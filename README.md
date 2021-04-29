# Scala Play! Framework Reimplementation
Reimplemented the [Movie Journal flask application](https://oss.navercorp.com/jiyouny-kim/Sentiment_Analysis) to the Scala Play! framework

### Feature
- Create journals on different movies
- Upload entries into each journal and have it's sentiment classified (pos/neutral/neg)

### Dependencies
- Postgres local db
- Tensorflow Java 0.2.0
- Scala 2.13.3

### Run Application

1. Set up Local Postgres DB
    
    - On your local device create Postgres Database and Tables according to files in the **sql** directory

2. Download [Model](https://kr1-link.ndrive.navercorp.com/folderLink/aGVxWTdWVVc5bXAzZnlVdHQ2NkVBNHMvWVJ0dXh5OGc5elBMUWNua0JaWVhCSGwxelFBVUVGMnl0eTVTS1BaZURBPT0), move model into **public** directory 

![filepath image][logo]

[logo]: https://oss.navercorp.com/jiyouny-kim/Scala/blob/master/journal/model_directory_filepath.png
"Logo Title Text 2"

3. ```sbt start``` 
4. Check it out http://localhost:9000/



### Java Tensorflow 0.2.0 Serving 
Helpful resources

- https://stackoverflow.com/questions/62241546/java-tensorflow-keras-equivalent-of-model-predict
- https://github.com/tensorflow/java/issues/134
- https://github.com/tensorflow/java/tree/master/ndarray
- https://stackoverflow.com/questions/60783153/tensorflow-in-scala-reflection
- https://github.com/tensorflow/java/pull/92

MAJOR ISSUE: 

Model accuracy that can be observed through the application is VERY different from the behavior seen on the flask application although the exact same model was used. 

플라스크로 서빙했던 모델이랑 동일한 모델이지만 어프리케이션상 보이는 accuracy가 예상한 봐와 너무 다릅니다.

### Issues
- Slow initial application loading time
- High Physical Memory usage
- Model accuracy not as expected

