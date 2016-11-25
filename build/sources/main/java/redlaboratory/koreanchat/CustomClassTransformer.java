package redlaboratory.koreanchat;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.launchwrapper.IClassTransformer;

public class CustomClassTransformer implements IClassTransformer {

	private static String[] classes = {
			"net.minecraft.client.gui.GuiTextField",
			"net.minecraft.client.gui.inventory.GuiEditSign",
			"net.minecraft.client.gui.GuiChat"
	};
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		for (String className : classes) {
			if (transformedName.equals(className)) {
				basicClass = getBytesInJar(transformedName, basicClass, CustomFMLLoadingPlugin.location);
				
				break;
			}
		}
		
		return basicClass;
	}
	
	public byte[] getBytesInJar(String classPath, byte[] def, File jarFile) {
		byte[] bytes = def;
		
		try {
			ZipFile zip = new ZipFile(jarFile);
			ZipEntry entry = zip.getEntry(classPath.replace('.', '/') + ".class");
			
			if (entry == null) {
				System.out.println(classPath + " doesn't exist in " + jarFile.getName());
			} else {
				InputStream in = zip.getInputStream(entry);
				bytes = new byte[(int) entry.getSize()];
				
				in.read(bytes);
				in.close();
				
				System.out.println("[KoreanChatMod]: " + "Class " + classPath + " has been patched!");
			}
			
			zip.close();
		} catch (Exception e) {
			throw new RuntimeException("Error occured when overriding " + classPath + " to " + jarFile.getName(), e);
		}
		
		return bytes;
	}

}
