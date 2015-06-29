package com.xbarr.movies

import collection.GenIterable
import collection.parallel.immutable.ParSeq
import collection.immutable.Seq
import edu.illinois.cs.cogcomp.lbjava.nlp.seg.PlainToTokenParser;
import edu.illinois.cs.cogcomp.lbjava.nlp.Word;
import edu.illinois.cs.cogcomp.lbjava.nlp.WordSplitter;
import edu.illinois.cs.cogcomp.lbjava.parse.Parser
import edu.illinois.cs.cogcomp.lbj.chunk.Chunker

class LineReader(input:GenIterable[String]) extends Parser {
  import edu.illinois.cs.cogcomp.lbjava.nlp.Sentence
  
  var iterator = getIterator
  
  def getIterator = input.iterator
  
  def next = if(iterator.hasNext) new Sentence(iterator.next) else null
  
  def reset:Unit = {this.iterator = getIterator}
  
  def close:Unit = {}
  
}

class FunctionalParser(p:Parser) extends PlainToTokenParser(p) {
  import edu.illinois.cs.cogcomp.lbjava.parse.LinkedVector;
  import edu.illinois.cs.cogcomp.lbjava.nlp.seg.Token
  
  def collect = {
    val sentences = collection.mutable.MutableList[Seq[Token]]()
    var _next = this.next()
    while(_next != null){
      val sentence = collection.mutable.MutableList[Token]()
      var word = _next.asInstanceOf[Token]
      while(word.next != null){
        sentence += word
        word = word.next.asInstanceOf[Token]
        _next = this.next()
      }
      sentence += word
      sentences += sentence.to[Seq]
      _next = this.next()
    }
    sentences.to[ParSeq]
  }
}


case class Chunk(chunkLabel:String,words:Seq[Word],span:String=null)
case class ChunkedSentence(sentence:String,chunks:Seq[Chunk])

object Parse {
  
  def getChunker = new Chunker
  
  private lazy val chunker = getChunker
  
  def predict(w:Word) = chunker.discreteValue(w)
  
  def getParser(input:GenIterable[String]) =
    new FunctionalParser(new WordSplitter(new LineReader(input)))
  
  def isNewChunk(chunkLabel:String,previousLabel:String) =
    chunkLabel == "O" ||
    (chunkLabel.startsWith("B-") ||
    chunkLabel.startsWith("I-") &&
    previousLabel != chunkLabel.substring(2)) 
    
  def nameChunk(chunkLabel:String) = 
    if(chunkLabel != "O") chunkLabel.substring(2) else chunkLabel
  
  def createChunk(chunk:(String,Word)) = 
    Chunk(nameChunk(chunk._1),Seq(chunk._2))
  
  def appendToChunk(chunk:Chunk,w:Word) = 
    Chunk(chunk.chunkLabel,chunk.words :+ w)
    
  def chunkSentences(entries:GenIterable[String]) =
    entries.zip(chunk(entries)).map{ x => ChunkedSentence(x._1,x._2) }.to[ParSeq]
  
  def addSpan(chunk:Chunk) =
    Chunk(chunk.chunkLabel,chunk.words,chunk.words map {_.form} reduce{_+" "+_})
  
  def chunk(entries:GenIterable[String]) = getParser(entries).collect map { tokens =>
      val labeledTokens = tokens.map {w => (predict(w),w)}
      labeledTokens.tail.foldLeft(Seq(createChunk(labeledTokens.head))
       )({case(chunks,(chunkLabel,word)) =>
          if(isNewChunk(chunkLabel,chunks.last.chunkLabel))
            chunks :+ createChunk(chunkLabel,word)
          else
            chunks.dropRight(1) :+ appendToChunk(chunks.last,word)
          })} map {_ map addSpan }
  
}