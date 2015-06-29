package com.xbarr.movies

import scala.io.Source
import com.xbarr.movies.Constants._
import scala.collection.immutable.Seq
import scala.collection.immutable.Set
import scala.collection.parallel.immutable.ParSeq
import java.io.{FileInputStream,File}
import java.nio.channels.FileChannel
import java.nio.MappedByteBuffer
import java.nio.charset.Charset

object Ingest {
  
   def getEntries = readFile(dataPath).split("\r\n").to[ParSeq]
   
   def wordList = 
    readFile("/usr/share/dict/words").split("\n").to[Set]
   
   def readFile(path:String) = {
     val fc = new FileInputStream(new File(path)).getChannel
     val bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
     Charset.defaultCharset().decode(bb).toString
   } 
   
}