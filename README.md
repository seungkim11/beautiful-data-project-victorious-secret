# Data Science Project - Spring 2016

[![Build Status](https://travis-ci.org/csula/datascience-spring-2016.svg?branch=master)](https://travis-ci.org/csula/datascience-spring-2016)

**Team Members:**  
Seung Kim | Tony Guardado  

* In this project, we collected 3.5 million Reddit posts and analyzed factors that determines post popularity.  
* Data acquisition was done by scheduled remote virtual instances in Google Compute Engine.  
* To provide enough memory and performance, configuration of 5 distributed servers in Elastic Search cluster was implemented.
* Analyzed sentiment scores of comments in each posting
* Conducted machine learning classification algorithm to predict post popularity

### Tools Used:
* Java, Python, MongoDB
* Elastic Search Kibana
* [TextBlob][4] for sentiment Analysis
* Pandas, Scipy, Scikit-learn libraries for machine learning

## Quick links
* [Visual Analysis Video][1]
* [Presentation Slides][2]
* [Machine Learning Implementations][3]

[1]: https://youtu.be/n615gJwOKaw
[2]: https://github.com/seungkim11/beautiful-data-project-victorious-secret/blob/master/presentation.pdf
[3]: https://github.com/seungkim11/beautiful-data-project-victorious-secret/blob/master/python/Reddit_Analysis.ipynb
[4]: http://textblob.readthedocs.io/en/dev/
