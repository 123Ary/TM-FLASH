package com.tmtl_ecu;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateFiles {


    public String splitFile(File f) throws IOException {
        int partCounter = 1;

        int sizeOfFiles = (1024 * 1024)/2;// 1MB

        byte[] buffer = new byte[sizeOfFiles];


        String fileName="File";
        int i=1;

        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                String tempname=fileName+i;
                String newfilename=tempname+".bin";

                String filePartName = newfilename;
                File newFile = new File(f.getParent(), filePartName);

                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
                i++;
            }
        }
        return  "success";
    }
    public String generateBin(byte[] buffer, String outFile, String inFile)
    {
        File f=new File(outFile);
        File newFile = new File(f.getParent(), inFile);
        try (FileOutputStream out = new FileOutputStream(newFile)) {
            out.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
