Derek Shaw Submissions For the Sortable Coding Challenge
========================================================

Quick Start
-----------

To run the submission against the published challenge data, run

    $ make

This will compile the code, run the test cases, download the challenge data and 
unpack it, run the submission against the challenge data, then validate the 
output against `challenge-check.sortable.com/validate`.

To only compile and execute, assuming `products.txt` and `listings.txt` are in
the local directory, execute:

    $ make compile results.txt


General Introduction
--------------------

All data is read in from `products.txt` and `listings.txt`. The data is 
normalized to remove unnecessary spacing, strip accents, convert uppercase
characters to lowercase, and remove punctuation.

The normalized data is then passed into `MatchingEngine`. The matching engine 
trivially divides the listings into 8 groups. Each group of data is then passed
off to a thread to calculate match scores. 

Because I am not well versed in the problem space, I felt it would be most fun 
to implement deterministic matching. I use the Levenshtein matching algorithm to
perform fuzzy substring matches. This allows minor discrepancies between product
names, families, and models and listings. I expect that listings don't always 
faithfully represent model names. For instance, the Canon 130 IS might be listed
as "130IS".

A score for a product-listing pair is the Levenshtein edit distance of the 
product's model to the listing title, the lowest value of the product's 
manufacturer to the listing title or the product's manufacturer to the 
listing's manufacturer, and the product's family (if specified) to the 
listing's title. 

A listing matches a product as long as there is only one product for the lowest 
matched score, and as long as the score is less than or equal to 3. (The 
threshold of 3 was arbitrarily chosen)


Performance
-----------

The execution performance of my solution is less than what I had hoped for.
Profiling shows that Levenshtein substring matching takes the most amount of 
processing time, as would be expected. Simple substring matching runs much more 
quickly but at the expense of having much fewer matches. It is unclear how 
critical good performance is to the coding challenge. 

I am reasonably happy with the matches that the code was able to produce.
Running `make debug` will generate `products.html` and `listings.html` which will 
show a list of products and for each product, will show the lowest scoring 
listings for that product (or vice-versa).


Improvements
------------

Given infinite time and a lack of other responsibilities, I would enjoy 
continuing to work on this challenge. I would want to create a test set of data 
with known, correct matches for assessing matching accuracy and recall, and for 
benchmarking the performance of the matching engine. Without the former, it 
would be unclear if any changes are an improvement.

The most obvious improvement to my machine engine would be to make use of the 
cost data. Assuming the cost of a product follows a standard distribution, a 
deterministic matcher could use the probability of the cost falling into a 
product's cost as a way to distinguish between two products with the same score.