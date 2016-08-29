DIR=$(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))

CHALLENGE_DATA=challenge_data_20110429.tar.gz
JUNIT_LIBS=$(DIR)/libs/junit-4.12.jar:$(DIR)/libs/hamcrest-core-1.3.jar
LIBS=$(DIR)/libs/json-simple-1.1.1.jar:$(DIR)/libs/log4j-api-2.6.2.jar:$(DIR)/libs/log4j-core-2.6.2.jar:$(DIR)/libs/mockito-all-1.10.19.jar:$(JUNIT_LIBS)

.PHONY: all
all: test validate

$(CHALLENGE_DATA):
	wget https://s3.amazonaws.com/sortable-public/challenge/$(CHALLENGE_DATA) -o /dev/null

.PHONY: txt
txt: $(CHALLENGE_DATA)
	@set -e ;					\
	if [ ! -f products.txt ] 			\
	 || [ ! -f listings.txt ] 			\
	 || [ $(CHALLENGE_DATA) -nt products.txt ] 	\
	 || [ $(CHALLENGE_DATA) -nt listings.txt ]; 	\
	then 						\
		echo "tar --touch -zxf $<"; 		\
		tar --touch -zxf $<; 			\
	fi

.PHONY: compile
compile: tags
	@mkdir -p $(DIR)/obj
	javac $(DIR)/src/*.java -classpath $(LIBS):$(JUNIT_LIBS) -d $(DIR)/obj -Xlint:cast -Xlint:deprecation -Xlint:divzero -Xlint:empty -Xlint:finally -Xlint:overrides -Xlint:path -Xlint:serial -Xlint:unchecked

results.txt: compile txt
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/log4j2.xml Main products.txt listings.txt > $@

.PHONY: debug
debug: compile txt
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/log4j2.xml Main --debug products.txt listings.txt > results.txt

.PHONY: validate
validate: results.txt
	curl -XPOST -F file=@results.txt https://challenge-check.sortable.com/validate

# Potential improvement: the test code should be moved to a test directory
.PHONY: test
test: compile
	@# Improvement: use a $( find )
	java -cp $(DIR)/obj:$(LIBS) \
	     org.junit.runner.JUnitCore \
	     AccentNormalizerTest \
	     DeterministicMatcherTest \
	     IdentityNormalizerTest \
	     LevenshteinTest \
	     LowercaseNormalizerTest \
	     MainTest \
	     MatchingEngineTest \
	     NormalizedListingTest \
	     NormalizedProductTest \
	     RegexNormalizerTest \
	     SubstringReplacerNormalizerTest \
	     TrimNormalizerTest

tags: src/*.java
	find . -name "*.java" | ctags --language-force=java -ftags -L-


# Improvement: The panasonic, canon, and sony alisas need to be abstracted out
datasets/panasonic/results.txt: compile
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/datasets/log4j2.xml Main datasets/panasonic/products.txt datasets/panasonic/listings.txt > $@

# Alias
.PHONY: panasonic
panasonic: datasets/panasonic/results.txt

datasets/canon/results.txt: compile
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/datasets/log4j2.xml Main --debug datasets/canon/products.txt datasets/canon/listings.txt > $@

# Alias
.PHONY: canon
canon: datasets/canon/results.txt

datasets/sony/results.txt: compile
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/datasets/log4j2.xml Main --debug datasets/sony/products.txt datasets/sony/listings.txt > $@

# Alias
.PHONY: sony
sony: datasets/sony/results.txt

datasets/utf-8/results.txt: compile
	java -cp $(DIR)/obj:$(LIBS) -Dlog4j.configurationFile=$(DIR)/datasets/log4j2.xml Main --debug datasets/utf-8/products.txt datasets/utf-8/listings.txt > $@

# Alias
.PHONY: utf-8 
utf-8: datasets/utf-8/results.txt


.PHONY: clean
clean:
	-rm -rf \
	    $(CHALLENGE_DATA) \
	    *.txt \
	    datasets/canon/results.txt \
	    datasets/panasonic/results.txt \
	    datasets/sony/results.txt \
	    datasets/utf-8/results.txt \
	    listings.html \
	    obj/ \
	    products.html \
	    tags

