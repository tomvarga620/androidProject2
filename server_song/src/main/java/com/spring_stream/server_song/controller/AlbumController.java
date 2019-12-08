package com.spring_stream.server_song.controller;

import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.model.Album;
import com.spring_stream.server_song.repozitory.AlbumRepozitory;
import com.spring_stream.server_song.service.AlbumService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    private PrimitiveSecurity primitiveSecurity = PrimitiveSecurity.getInstance();

    @PostMapping(value = "/insertAlbum")
    public Album insertAlbum(@RequestBody Album album) {
        return albumService.insertAlbum(album);
    }

    @GetMapping(value = "/getAlbum", produces = MediaType.APPLICATION_JSON_VALUE)
    public Album getCertainAlbum(@RequestParam Long id) {
        return  albumService.getAlbum(id);
    }

    @GetMapping(value = "/getAllAlbums", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Album> getAllDistinctAlbum() {
        return (List<Album>) albumService.getAllAlbums();
    }

    @RequestMapping(value = "/getAlbumCover", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam Long id) throws IOException {
        File image = new File(albumService.findImagePath(id));
        InputStream in = new FileInputStream(image);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
