package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redlaboratory.koreanchat.KoreanCore;
import redlaboratory.koreanchat.Result;

import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiEditSign extends GuiScreen
{
    /** Reference to the sign object. */
    private final TileEntitySign field_146848_f;
    /** Counts the number of screen updates. */
    private int field_146849_g;
    /** The index of the line that is being edited. */
    private int field_146851_h;
    /** "Done" button for the GUI. */
    private GuiButton field_146852_i;

    public GuiEditSign(TileEntitySign p_i1097_1_)
    {
        this.field_146848_f = p_i1097_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        Keyboard.enableRepeatEvents(true);
        this.field_146852_i = this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, I18n.func_135052_a("gui.done", new Object[0])));
        this.field_146848_f.func_145913_a(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient nethandlerplayclient = this.field_146297_k.func_147114_u();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.func_147297_a(new CPacketUpdateSign(this.field_146848_f.func_174877_v(), this.field_146848_f.field_145915_a));
        }

        this.field_146848_f.func_145913_a(true);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void func_73876_c()
    {
        ++this.field_146849_g;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 0)
            {
                this.field_146848_f.func_70296_d();
                this.field_146297_k.func_147108_a((GuiScreen)null);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 200)
        {
            this.field_146851_h = this.field_146851_h - 1 & 3;
        }

        if (p_73869_2_ == 208 || p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.field_146851_h = this.field_146851_h + 1 & 3;
        }

        String s = this.field_146848_f.field_145915_a[this.field_146851_h].func_150260_c();

        if (p_73869_2_ == 14 && !s.isEmpty())
        {
        	Result r = KoreanCore.deleteKorean(s, s.length(), s.length());// XXX
        	s = r.STR;
//            s = s.substring(0, s.length() - 1);
        }

        if (p_73869_2_ == 29) {// XXX
        	KoreanCore.switchInputMode();
        }
        
        if (ChatAllowedCharacters.func_71566_a(p_73869_1_) && this.field_146289_q.func_78256_a(s + p_73869_1_) <= 90)
        {
        	Result r = KoreanCore.writeKorean(s, s.length(), s.length(), p_73869_1_);// XXX
        	s = r.STR;
//            s = s + typedChar;
        }

        this.field_146848_f.field_145915_a[this.field_146851_h] = new TextComponentString(s);

        if (p_73869_2_ == 1)
        {
            this.func_146284_a(this.field_146852_i);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.func_135052_a("sign.edit", new Object[0]), this.field_146294_l / 2, 40, 16777215);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.field_146294_l / 2), 0.0F, 50.0F);
        float f = 93.75F;
        GlStateManager.func_179152_a(-93.75F, -93.75F, -93.75F);
        GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = this.field_146848_f.func_145838_q();

        if (block == Blocks.field_150472_an)
        {
            float f1 = (float)(this.field_146848_f.func_145832_p() * 360) / 16.0F;
            GlStateManager.func_179114_b(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int i = this.field_146848_f.func_145832_p();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.func_179114_b(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -1.0625F, 0.0F);
        }

        if (this.field_146849_g / 6 % 2 == 0)
        {
            this.field_146848_f.field_145918_i = this.field_146851_h;
        }

        TileEntityRendererDispatcher.field_147556_a.func_147549_a(this.field_146848_f, -0.5D, -0.75D, -0.5D, 0.0F);
        this.field_146848_f.field_145918_i = -1;
        GlStateManager.func_179121_F();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
