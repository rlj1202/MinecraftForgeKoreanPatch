package redlaboratory.koreanchat;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

public class KoreanFontRenderer {
	
	private static HashMap<String, int[]> fontTextures;
	
	static {
		fontTextures = new HashMap<String, int[]>();
		
//		KoreanFontRenderer.uploadFont("NanumGothicBold", 12, 1, true);
		int charSize = 15;
		KoreanFontRenderer.uploadFont("NanumGothic", charSize, (int) (charSize * 3.0f / (charSize + 2)), true);
	}
	
	@Deprecated
	public static void testFont(String fontName, int size, int x, int y) {
		glDisable(GL_TEXTURE_2D);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glBegin(GL_QUADS);
		{
			glVertex2f(x       , y       );
			glVertex2f(x       , y + size);
			glVertex2f(x + size, y + size);
			glVertex2f(x + size, y       );
		}
		glEnd();
		
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, fontTextures.get(fontName)[0xac]);
		glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 1); glVertex3f(x       , y       , 0.0f);
			glTexCoord2f(0, 0); glVertex3f(x       , y + size, 0.0f);
			glTexCoord2f(1, 0); glVertex3f(x + size, y + size, 0.0f);
			glTexCoord2f(1, 1); glVertex3f(x + size, y       , 0.0f);
		}
		glEnd();
	}
	
	public static void uploadFont(String fontName, int charSize, int margin, boolean antialias) {
		int[] textures = new int[0xf00];
		int sellSize = charSize + margin * 2;
		int textureSize = sellSize * 16;
		
		for (int i = 0; i <= 0xff; i++) {
			BufferedImage fontImage = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_INT_ARGB);
			
			{
				Graphics2D gt = (Graphics2D) fontImage.createGraphics();
				
				if (antialias) gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				else gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				
				gt.setFont(new Font(fontName, Font.PLAIN, charSize));
				gt.setColor(Color.WHITE);
				
				for (int a = 0; a < 16; a++) {
					for (int b = 0; b < 16; b++) {
						char c = (char) ((i << 8) + a * 16 + b);
						
						gt.drawString(Character.toString(c), margin + sellSize * b, sellSize * (a + 1) - margin * 2);
					}
				}
				
				gt.dispose();
			}
			
			{
				int textureID = glGenTextures();
				textures[i] = textureID;
				
				glBindTexture(GL_TEXTURE_2D, textureID);
				
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, fontImage.getWidth(), fontImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, getByteBuffer(fontImage));
			}
		}
		
		fontTextures.put(fontName, textures);
	}
	
	public static void renderString(String str, int size, float x, float y, String fontName) {
		int interval = size;
		
		for (int i = 0; i < str.length(); i++) {
			renderChar(str.charAt(i), size, x + interval * i, y, fontName, false);
		}
	}
	
	public static void renderString(String str, int size, int interval, float x, float y, String fontName) {
		for (int i = 0; i < str.length(); i++) {
			renderChar(str.charAt(i), size, x + interval * i, y, fontName, false);
		}
	}
	
	public static void renderChar(char c, int size, float x, float y, String fontName, boolean italic) {
		int textureIndex = c >>> 8;
		
		int fontX = 0x00ff & c;
		int fontY = 0;
		
		while (fontX >= 16) {
			fontX -= 16;
			fontY++;
		}
		
		float italicF = italic ? size / 3.0f: 0;
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindTexture(GL_TEXTURE_2D, fontTextures.get(fontName)[textureIndex]);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(fontX       / 16.0f, (fontY + 1) / 16.0f); glVertex2f(x                 , y + size);
			glTexCoord2f((fontX + 1) / 16.0f, (fontY + 1) / 16.0f); glVertex2f(x + size          , y + size);
			glTexCoord2f((fontX + 1) / 16.0f, fontY       / 16.0f); glVertex2f(x + size + italicF, y       );
			glTexCoord2f(fontX       / 16.0f, fontY       / 16.0f); glVertex2f(x        + italicF, y       );
		}
		glEnd();
		glDisable(GL_BLEND);
	}
	
	private static ByteBuffer getByteBuffer(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[x + y * image.getWidth()];
				buffer.put((byte) ((pixel >> 16) & 0xff));
				buffer.put((byte) ((pixel >> 8) & 0xff));
				buffer.put((byte) (pixel & 0xff));
				buffer.put((byte) ((pixel >> 24) & 0xff));
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	
}
