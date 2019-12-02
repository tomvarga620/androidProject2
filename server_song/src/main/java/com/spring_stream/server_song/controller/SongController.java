package com.spring_stream.server_song.controller;

import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.service.SongService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
public class SongController {

    @Autowired
    private SongService songService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(path = "/insertSong", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insert(@RequestBody Song song) {
        Song insSong = songService.insert(song);
        return insSong.toString();
    }

    @GetMapping(value = "/getAllSongs",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Song> findAll() {
        return (List<Song>) songService.getAllSongs();
    }

    @RequestMapping(value = "/getSongCover", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam String id) throws IOException {
        File image = new File(songService.findImagePath(id));
        InputStream in = new FileInputStream(image);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @RequestMapping(value="streamSong", method=RequestMethod.GET)
    public void getDownload(HttpServletResponse response, @RequestParam String id) {
        File f = new File(songService.findPath(id));
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Set the content type and attachment header.
        String contentType = response.getContentType();

        //audio/mpeg
        response.addHeader("Content-Disposition:", "attachment;filename=\"" + f.getName() + "\"");
        response.setContentType(contentType);

        // Copy the stream to the response's output stream.
        try {
            IOUtils.copy(targetStream, response.getOutputStream());
            //General IO stream manipulation utilities.
            //This class provides static utility methods for input/output operations.
            //copy - these methods copy all the data from one stream to another

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.flushBuffer();
            //Forces any content in the buffer to be written to the client
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
