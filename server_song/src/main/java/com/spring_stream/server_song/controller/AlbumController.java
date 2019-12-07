package com.spring_stream.server_song.controller;

import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.repozitory.AlbumRepozitory;
import com.spring_stream.server_song.service.AlbumService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    private PrimitiveSecurity primitiveSecurity = PrimitiveSecurity.getInstance();

    @RequestMapping(value = "/getAlbumCovers", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam Long id) throws IOException {
        File image = new File(albumService.findImagePath(id));
        InputStream in = new FileInputStream(image);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
