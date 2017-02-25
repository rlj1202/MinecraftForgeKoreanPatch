package redlaboratory.koreanchat;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = CustomFMLLoadingPlugin.VERSION)
public class CustomFMLLoadingPlugin implements IFMLLoadingPlugin {
	
	public static final String VERSION = "1.11";
	public static File location;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {CustomClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return CustomDummyModContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
