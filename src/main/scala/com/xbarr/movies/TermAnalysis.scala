package com.xbarr.movies

import scala.collection.parallel.immutable.ParSeq
import scala.collection.immutable.Seq
import scala.collection.immutable.Set
import com.xbarr.movies.Constants._
import org.allenai.nlpstack.tokenize.defaultTokenizer
import org.allenai.nlpstack.lemmatize.MorphaStemmer



object TermAnalysis {
  
  private lazy val lemmatizer = new MorphaStemmer

  def counts[T](s: Seq[T]) = s.groupBy(identity).mapValues(_.length)
     
  def getTerms(docs:ParSeq[Map[String,Int]]) = 
    docs.flatten.groupBy{_._1} mapValues{_ map {_._2} reduce {_+_} }
       
  def getPositives(docs:ParSeq[Map[String, Int]]) = 
    docs filter {_.contains(seedTerm)}
     
  def chi2(docsIt:ParSeq[Map[String,Int]], filterCount:Int=0) = {
    val docs = docsIt.to[ParSeq]
    val terms = getTerms(docs).to[ParSeq]
    val positives = getPositives(docs)
    terms.filter{_._2 >= filterCount}.map{ case(term,termCount) =>
      (term ,
        { //N is the number of documents
          val N = docs.size.toDouble
          //A is number of times term and class co-occur
          val A = positives.filter{_.contains(term)}.size.toDouble
          //B is the number of times term occurs without class
          val B = termCount - A
          //C is number of times class occurs without term
          val C = positives.size - A
          //D is the number of times neither c nor t occurs
          val D = N - (A+B+C)
          N * (math.pow((A*D - C*B),2))  /  (A+C)*(B+D)*(A+B)*(C+B)})
       }.seq.sortBy(_._2).reverse
     }
  
  def getTopTerms(scoredTerms:Seq[(String,Double)]) =
    scoredTerms take((scoredTerms.size * topTerms).toInt) map {_._1}
  
  def stem(s:String) = lemmatizer.stem(s)
  
  def normalize(s:String) = s.replaceAll("[\n\r]", "").trim.toLowerCase
  
  def tokenize(s:String) = defaultTokenizer.tokenize(s).map{_.string}.to[Seq]
  
  def separateProperNouns(terms:Seq[String]) = {
    val termSet = terms.toSet
    val words = com.xbarr.movies.Ingest.wordList map stem map normalize
    val pns = termSet &~ words
    (pns,termSet &~ pns)
  }
  
  def tfIdf(docs:ParSeq[Map[String,Int]]) = {
     val terms = getTerms(docs)
     val idfs = terms map {{case(term,docFreq) => 
       term -> math.log(docs.size.toDouble / docFreq.toDouble)}}
     docs map { doc => {
       val docSize = doc map {_._2} reduce {_+_}
       doc map {{ case(term,count) =>
         term -> (count.toDouble / docSize.toDouble) * idfs(term) 
       }}
     }}
   }
  
}