organization := "XBarr Data Consulting"

name := "Movie Name Extractor"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.allenai.nlpstack" % "nlpstack-core_2.11" % "1.11"
libraryDependencies += "org.allenai.nlpstack" % "nlpstack-parse_2.11" % "1.11"
libraryDependencies += "org.allenai.nlpstack" % "nlpstack-postag_2.11" % "1.11"
libraryDependencies += "org.allenai.nlpstack" % "nlpstack-chunk_2.11" % "1.11"

resolvers += "CogcompSoftware" at "http://cogcomp.cs.illinois.edu/m2repo/"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "0.9.28" % "runtime"
libraryDependencies += "edu.illinois.cs.cogcomp" % "coreUtilities" % "0.2"
libraryDependencies += "edu.illinois.cs.cogcomp" % "edison" % "0.7.4"
libraryDependencies += "edu.illinois.cs.cogcomp" % "illinois-pos" % "2.0.1"
libraryDependencies += "org.apache.thrift" % "libthrift" % "0.8.0"
libraryDependencies += "edu.illinois.cs.cogcomp" % "curator-interfaces" % "0.7"
libraryDependencies += "edu.illinois.cs.cogcomp" % "illinois-chunker" % "2.0.0"
libraryDependencies += "edu.illinois.cs.cogcomp" % "illinois-lemmatizer" % "0.0.9"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"
libraryDependencies += "edu.illinois.cs.cogcomp" % "curator-utils" % "0.0.4-SNAPSHOT"
libraryDependencies += "edu.illinois.cs.cogcomp" % "illinois-ner" % "2.8.1"
libraryDependencies += "edu.illinois.cs.cogcomp" % "LBJava" % "1.0.3"
libraryDependencies += "edu.illinois.cs.cogcomp" % "illinois-common-resources" % "1.1" % "runtime"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1"
