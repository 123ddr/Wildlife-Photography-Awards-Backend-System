package com.wildlifebackend.wildlife.entitiy;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private OpenUser openUser;


    @Lob
    @Column(nullable = false)
    private byte[] rawFile;  // Store image as byte array

    private String description;

    private String title;


////    Storing large images in a database may slow performance. Instead, save them in a file storage (AWS S3, local file system) and store only the file path in the database
//    private String filePath;

}
