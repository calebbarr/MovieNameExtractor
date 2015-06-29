### Given a sample of utterances from an automated speech recognizer (ASR), build a system for the extraction of movie names, using only the data in the sample. 

The purpose of this assignment is to exercise your skills in building systems when pre-annotated data is not available.  Using 3rd party data would defeat the purpose of the exercise, so movie gazetteer lists are not allowed.

## Instructions (OSX):
* Clone this repo:
	* 	`git clone https://github.com/calebbarr/movies.git`
* Move ASR utterances into data directory:
	* 	`cd MovieNameExtractor && mv path/to/utterances.txt data/`
* Using [homebrew](http://brew.sh/), install [sbt](http://www.scala-sbt.org/):
	* 	`brew install sbt`
* Run extraction:
	* `bash run.sh`


