name := "MySQL - Jasper - Checkboxes ALL - Mailer"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean).settings {
  
 libraryDependencies ++= Seq(
    jdbc,
    evolutions,
    "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3",   // Bootstrap Adrian Validation & Contraints
    "org.webjars" % "font-awesome" % "4.5.0",
    "org.webjars" % "bootstrap-datepicker" % "1.4.0",
    "com.typesafe.play" %% "play-mailer" % "5.0.0-M1",     // Play Mailer Module
    "mysql" % "mysql-connector-java" % "5.1.39",           // MySQL Driver
    "net.sf.jasperreports" % "jasperreports" % "5.5.0",    // Jasper & other librairies
    "org.olap4j" % "olap4j" % "1.2.0",
    "net.sourceforge.barbecue" % "barbecue" % "1.5-beta1",
    "commons-codec" % "commons-codec" % "1.4",
    "com.lowagie" % "itext"  % "2.1.7",
    "commons-digester" % "commons-digester"  % "2.1",
    "net.sourceforge.jexcelapi" % "jxl"  % "2.6.12",
    "org.apache.poi" % "poi" % "3.9",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.1.0", // Json Body Request
    "com.github.xuwei-k" % "html2image" % "0.1.0"          // HTML To Image
    )

}


// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           

// Use .class files instead of generated .scala files for views and routes 
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  

// Compile the project before generating Eclipse files, so that .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)                  
