package com.spring_stream.server_song.controller;

import com.spring_stream.security.Credencials;
import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.model.Album;
import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.service.AlbumService;
import com.spring_stream.server_song.service.SongService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.function.Consumer;

@RestController
public class SongController {

    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;

    private PrimitiveSecurity primitiveSecurity = PrimitiveSecurity.getInstance();

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "/insertSong", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insertSong(@RequestBody Song song) {

        Album album = new Album();
        if (albumService.needToCreatedAlbum(song.getAlbum().getAlbumName())){
            album = albumService.insertAlbum(song.getAlbum());
        }else {
            album = albumService.getAlbumByName(song.getAlbum().getAlbumName());
        }
        song.setAlbum(new Album(album.getId()));
        Song insSong = songService.insert(song);
        return insSong.toString();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "/insertSongs", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insertMultiple(@RequestBody Iterable<Song> songs) {

       for (Song song: songs) {
           Album album = new Album();
           if (albumService.needToCreatedAlbum(song.getAlbum().getAlbumName())){
               album = albumService.insertAlbum(song.getAlbum());
           }else {
               album = albumService.getAlbumByName(song.getAlbum().getAlbumName());
           }
           song.setAlbum(new Album(album.getId()));
       }

        Iterable<Song> allSongs = songService.insertSongs(songs);
        return allSongs.toString();
    }

    @GetMapping(value = "/getAllSongs",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Song> findAll() {
        return (List<Song>) songService.getAllSongs();
    }

    @RequestMapping(value="streamSong", method=RequestMethod.GET)
    public ResponseEntity getDownload(HttpServletResponse response, @RequestParam Long id, @RequestBody Credencials credencials) {

        if (primitiveSecurity.accessTokens.get(credencials.getusername()).equals(credencials.getToken())) {
            File f = new File(songService.findPath(id));
            InputStream targetStream = null;
            try { targetStream = new FileInputStream(f); }
            catch (FileNotFoundException e) { e.printStackTrace(); }
            // Set the content type and attachment header.
            String contentType = response.getContentType();
            response.addHeader("Content-Disposition:", "attachment;filename=\"" + f.getName() + "\"");
            response.setContentType(contentType);
            // Copy the stream to the response's output stream.
            try { IOUtils.copy(targetStream, response.getOutputStream());
                //General IO stream manipulation utilities.
                //This class provides static utility methods for input/output operations.
                //copy - these methods copy all the data from one stream to another
            } catch (IOException e) { e.printStackTrace(); }
            try {
                response.flushBuffer();
                //Forces any content in the buffer to be written to the client
            } catch (IOException e) { e.printStackTrace(); }
        }else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
