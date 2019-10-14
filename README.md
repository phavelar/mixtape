# Mixtape

## This is my Java solution to Mixtape exercise.

## Requirements: 
[Java 8](https://www.java.com/en/) or newer and [Maven](https://maven.apache.org/) to build the executable jar file. 

## Generating the executable jar file:
1. Make sure java compiler is properly installed;
2. Download and install Maven;

compile Mixtape project as follows:

mvn compile assembly:single

## Running the executable jar file:

1. cd target
2. copy both mixtape-data.json and changes.json to this folder, otherwise you will need to enter the fullpath or relative path
to where these files are located in your system.
2. java -jar mixtapeApp.jar mixtape-data.json changes.json output.json

## Design Considerations

I choose Java for the challenge, perhaps a script language such as Groovy or Ruby would be better suited for this exercise.

There are 3 main packages: 
* domain -> POJO objects representing the mixtape model, here you will find Playlist, Song, User and Mixtape classes.
* Service -> Contain the processing elements, such as MixtapeService containing all operations asked in the exercise such as 
addSongToPlayList, addPlaylistToUser, etc. This package also contains ChangesFileProcessor.java which is in charge of dealing 
with the changes.json file parsing.
* main -> contains the application main function.

The mixtape-data.json is processed via [Jackson library](https://github.com/FasterXML/jackson), using the data-binding module which allows for mapping json nodes as annotated java POJOs.

### Changes File
The changes file has the following syntax:
```
{
  "addSongToPlaylist": [
    {
      "songId": "1",
      "playlistId": "3"
    },
    {
      "songId": "10",
      "playlistId": "3"
    }
  ],
  "addNewPlaylistToExistingUser": [
    {
      "user_id": "2",
      "song_ids": [
        "1",
        "2"
      ]
    },
    {
      "user_id": "3",
      "song_ids": [
        "5",
        "4",
        "10"
      ]
    }
  ],
  "removeExistingPlaylist": {
    "playlistIds": [
      "1",
      "2"
    ]
  }
}
```
The json structure has nodes that acs as commands followed by a json array acting as datum for each command.

To deal with this more dynamic format, I opted not to use a POJO mapped model, but instead to programmatically walk JSON nodes via jackson APIs.
This supports a very dynamic model for evolving the __changes.json__ file to support other operations on the __mixtape-data__ set, by adding code to accommodate new commands as opposed to deal with an ever evolving domain model.
For details, please see the __ChangesFileProcessor.java__  in the source tree. 
The domain model approach was used to serialize/deserialize the contents of __mixtape.json__ via Jackson Data-binding module, that is one POJO class per JSON node.
For details, please take a look under the domain package.

The code was mostly done using Test Driven Development (TDD) so, tests should cover most of the code.

## Future Enhancements/Changes required for scaling the application.
My presented solution works by reading the entire contents of the mixtape.json in memory, so for a very large file it won't work. 
So, I would need to walk the file using streaming APIs (similarly as I did with the changes.json file parsing), that is read it in chunks and process changes on these chunks, in a way that memory isn't ever an issue (however for large files it will become a slow process).
For such cases, a better solution is perhaps to leverage a document database, such as MongoDB or CouchDB. The application becomes trivial, just use the database native queries to modify the document. Let the database engine deal with replication/concurrency and follow its best practices for scaling needs.









