## Bitcoin administration app

This application serves two purposes: 

1. demonstrate the usage of a SpringWebMVC Java application, which uses ReactJS in the frontend
2. How to handle this with Maven
3. Serve as an actual application which might be useful to track Bitcoin information for yourself or your friends.


## Building
Just run

```
mvn package
```

and the following frontend commands will be executed in addition to the standard java commands: 

``` 
npm install
npm run-script build
npm run-script bundle
```

"build" runs the Babel JSX compilation, which is needed for reactjs. 

"bundle" runs Webpack which puts everything nicely into the file "resources/js/bundle.js"


