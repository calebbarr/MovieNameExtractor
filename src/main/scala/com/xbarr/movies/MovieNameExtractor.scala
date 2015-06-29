package com.xbarr.movies

import com.xbarr.movies.Ingest._
import com.xbarr.movies.Constants._
import com.xbarr.movies.TermAnalysis._
import com.xbarr.movies.Parse._


object Constants {
  val seedTerm = "the movie"
  val dataPath = "data/utterancesForMovies.txt"
  val topTerms = .2
  val stopWords = Set(
    "the",
    "question-mark",
    "tonight",
    "listings",
    "theater",
    "theaters",
    "times",
    "start",
    "starts",
    "tickets",
    "playing",
    "google",
    "schedule",
    "schedules",
    "trailer",
    "trailers",
    "ratings",
    "showing",
    "showings",
    "show",
    "cinema",
    "release",
    "play",
    "plex",
    "starring"
  )
}

object MovieNameExtractor {
  import collection.parallel.immutable.ParSeq

  def main(args:Array[String]):Unit={
    extractMovieTitles(
        chunkSentences(getEntries filter {_.contains(seedTerm)})
        ) foreach println
  }
  
  def extractMovieTitles(chunkedSentences:ParSeq[ChunkedSentence]) =
    chunkedSentences flatMap { 
      _.chunks filter {_.span contains seedTerm} map {chunk =>
        extractMovieTitle(splitOnSeedTerm(chunk.words map {_.form.trim}))
      }
    } filterNot {_.trim.isEmpty} filterNot mostlyStopwords distinct
  
  private def splitOnSeedTerm(words:Seq[String]) = 
      words.splitAt(words.indexWhere { word =>
          val split = words.splitAt(words.indexOf(word))
          (split._1.mkString(" ") == seedTerm ||
          split._2.mkString(" ") == seedTerm )
          })
  
  private def extractMovieTitle(split:(Seq[String], Seq[String]))=
    stripStopWords(
        if(split._1.mkString(" ") == seedTerm) split._2 
        else split._1).mkString(" ")
    
  private def stripStopWords(title:Seq[String]) =
    title.dropWhile{ w => 
      stopWords.contains(w)}.reverse.dropWhile{ w => 
        stopWords.contains(w)}.reverse
    
  def mostlyStopwords(title:String)={
      val tokens=title.split("\\s+")
      tokens.filter{stopWords.contains}.size.toDouble / tokens.size.toDouble >= 0.5
    }

}