package com.example.blackjack.Controllers;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class ImageConverter {
    public static byte[] convertSvgToPng(String svgFilePath, int CARD_WIDTH, int CARD_HEIGHT) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) CARD_WIDTH);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) CARD_HEIGHT);

        TranscoderInput input = new TranscoderInput(new FileInputStream(svgFilePath));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TranscoderOutput transcoderOutput = new TranscoderOutput(output);

        transcoder.transcode(input, transcoderOutput);

        byte[] pngImageData = output.toByteArray();
        output.close();

        return pngImageData;
    }
}
