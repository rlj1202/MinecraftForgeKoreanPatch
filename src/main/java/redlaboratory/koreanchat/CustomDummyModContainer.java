package redlaboratory.koreanchat;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import com.google.common.eventbus.EventBus;

public class CustomDummyModContainer extends DummyModContainer {
	
	public CustomDummyModContainer() {
		super(new ModMetadata());
		
		ModMetadata data = getMetadata();
		data.modId = "koreanchatmod";
		data.name = "Korean Chat Mod";
		data.description = "Korean chat mod.";
		data.version = "0.2.0";
		data.url = "http://redlaboratory.tistory.com/";
		data.authorList = Arrays.asList(new String[] {"RedLaboratory"});
		data.credits = "The mod to give a fuction to type korean characters.";
		data.logoFile = "";
		data.screenshots = new String[] {};
		data.dependencies = new ArrayList<ArtifactVersion>();
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		
		return true;
	}
	
}
