package com.spring_stream.server_song.controller;

import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.model.Album;
import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.service.AlbumService;
import com.spring_stream.server_song.service.SongService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongService songService;

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

    @RequestMapping(value = "/getAlbumCoverThumbnail", method = RequestMethod.GET)
    public void getImageThumbnail(HttpServletResponse response, @RequestParam Long id) throws IOException {
        File image = new File(albumService.findImagePath(id));
        Image thumbnail = ImageIO.read(image).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(thumbnail, 0, 0, null);
        bGr.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bimage,"png", os);
        InputStream in = new ByteArrayInputStream(os.toByteArray());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @RequestMapping(value = "/getSongImageThumbnail", method = RequestMethod.GET)
    public void getSongImageThumbnail(HttpServletResponse response, @RequestParam Long id) throws IOException {
        Song song = songService.findById(id);

        File image = new File(albumService.findImagePath(song.getAlbum().getId()));
        Image thumbnail = ImageIO.read(image).getScaledInstance(200, 200, BufferedImage.SCALE_SMOOTH);

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(thumbnail, 0, 0, null);
        bGr.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bimage,"png", os);
        InputStream in = new ByteArrayInputStream(os.toByteArray());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping(value = "getAlbumByIdSong",produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> getAlbumByIdSong(@RequestParam Long id){
        Album album = songService.findById(id).getAlbum();
        Map<String,Object> response = new HashMap<>();
        response.put("id",album.getId());
        response.put("albumName",album.getAlbumName());
        return response;
    }
}
