package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import redlaboratory.koreanchat.KoreanCore;
import redlaboratory.koreanchat.Result;

@SideOnly(Side.CLIENT)
public class GuiTextField extends Gui
{
    private final int field_175208_g;
    private final FontRenderer field_146211_a;
    public int field_146209_f;
    public int field_146210_g;
    /** The width of this text field. */
    public int field_146218_h;
    public int field_146219_i;
    /** Has the current text being edited on the textbox. */
    private String field_146216_j = "";
    private int field_146217_k = 32;
    private int field_146214_l;
    private boolean field_146215_m = true;
    /** if true the textbox can lose focus by clicking elsewhere on the screen */
    private boolean field_146212_n = true;
    /** If this value is true along with isEnabled, keyTyped will process the keys. */
    private boolean field_146213_o;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    private boolean field_146226_p = true;
    /** The current character index that should be used as start of the rendered text. */
    private int field_146225_q;
    private int field_146224_r;
    /** other selection position, maybe the same as the cursor */
    private int field_146223_s;
    private int field_146222_t = 14737632;
    private int field_146221_u = 7368816;
    /** True if this textbox is visible */
    private boolean field_146220_v = true;
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate<String> field_175209_y = Predicates.<String>alwaysTrue();

    public GuiTextField(int p_i45542_1_, FontRenderer p_i45542_2_, int p_i45542_3_, int p_i45542_4_, int p_i45542_5_, int p_i45542_6_)
    {
        this.field_175208_g = p_i45542_1_;
        this.field_146211_a = p_i45542_2_;
        this.field_146209_f = p_i45542_3_;
        this.field_146210_g = p_i45542_4_;
        this.field_146218_h = p_i45542_5_;
        this.field_146219_i = p_i45542_6_;
    }

    /**
     * Sets the GuiResponder associated with this text box.
     */
    public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_)
    {
        this.field_175210_x = p_175207_1_;
    }

    /**
     * Increments the cursor counter
     */
    public void func_146178_a()
    {
        ++this.field_146214_l;
    }

    /**
     * Sets the text of the textbox, and moves the cursor to the end.
     */
    public void func_146180_a(String p_146180_1_)
    {
        if (this.field_175209_y.apply(p_146180_1_))
        {
            if (p_146180_1_.length() > this.field_146217_k)
            {
                this.field_146216_j = p_146180_1_.substring(0, this.field_146217_k);
            }
            else
            {
                this.field_146216_j = p_146180_1_;
            }

            this.func_146202_e();
        }
    }

    /**
     * Returns the contents of the textbox
     */
    public String func_146179_b()
    {
        return this.field_146216_j;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String func_146207_c()
    {
        int i = this.field_146224_r < this.field_146223_s ? this.field_146224_r : this.field_146223_s;
        int j = this.field_146224_r < this.field_146223_s ? this.field_146223_s : this.field_146224_r;
        return this.field_146216_j.substring(i, j);
    }

    public void func_175205_a(Predicate<String> p_175205_1_)
    {
        this.field_175209_y = p_175205_1_;
    }

    /**
     * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
     */
    public void func_146191_b(String p_146191_1_)
    {
        String s = "";
        String s1 = ChatAllowedCharacters.func_71565_a(p_146191_1_);
        int i = this.field_146224_r < this.field_146223_s ? this.field_146224_r : this.field_146223_s;
        int j = this.field_146224_r < this.field_146223_s ? this.field_146223_s : this.field_146224_r;
        int k = this.field_146217_k - this.field_146216_j.length() - (i - j);

        if (!this.field_146216_j.isEmpty())
        {
            s = s + this.field_146216_j.substring(0, i);
        }

        int l;

        if (k < s1.length())
        {
            s = s + s1.substring(0, k);
            l = k;
        }
        else
        {
            s = s + s1;
            l = s1.length();
        }

        if (!this.field_146216_j.isEmpty() && j < this.field_146216_j.length())
        {
            s = s + this.field_146216_j.substring(j);
        }

        if (this.field_175209_y.apply(s))
        {
            this.field_146216_j = s;
            this.func_146182_d(i - this.field_146223_s + l);

            if (this.field_175210_x != null)
            {
                this.field_175210_x.func_175319_a(this.field_175208_g, this.field_146216_j);
            }
        }
    }

    /**
     * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
     * which case the selection is deleted instead.
     */
    public void func_146177_a(int p_146177_1_)
    {
        if (!this.field_146216_j.isEmpty())
        {
            if (this.field_146223_s != this.field_146224_r)
            {
                this.func_146191_b("");
            }
            else
            {
                this.func_146175_b(this.func_146187_c(p_146177_1_) - this.field_146224_r);
            }
        }
    }

    /**
     * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
     * in which case the selection is deleted instead.
     */
    public void func_146175_b(int p_146175_1_)
    {
        if (!this.field_146216_j.isEmpty())
        {
            if (this.field_146223_s != this.field_146224_r)
            {
                this.func_146191_b("");
            }
            else
            {
                boolean flag = p_146175_1_ < 0;
                int i = flag ? this.field_146224_r + p_146175_1_ : this.field_146224_r;
                int j = flag ? this.field_146224_r : this.field_146224_r + p_146175_1_;
                String s = "";

                if (i >= 0)
                {
                    s = this.field_146216_j.substring(0, i);
                }

                if (j < this.field_146216_j.length())
                {
                    s = s + this.field_146216_j.substring(j);
                }

                if (this.field_175209_y.apply(s))
                {
                    this.field_146216_j = s;

                    if (flag)
                    {
                        this.func_146182_d(p_146175_1_);
                    }

                    if (this.field_175210_x != null)
                    {
                        this.field_175210_x.func_175319_a(this.field_175208_g, this.field_146216_j);
                    }
                }
            }
        }
    }

    public int func_175206_d()
    {
        return this.field_175208_g;
    }

    /**
     * Gets the starting index of the word at the specified number of words away from the cursor position.
     */
    public int func_146187_c(int p_146187_1_)
    {
        return this.func_146183_a(p_146187_1_, this.func_146198_h());
    }

    /**
     * Gets the starting index of the word at a distance of the specified number of words away from the given position.
     */
    public int func_146183_a(int p_146183_1_, int p_146183_2_)
    {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }

    /**
     * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
     */
    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_)
    {
        int i = p_146197_2_;
        boolean flag = p_146197_1_ < 0;
        int j = Math.abs(p_146197_1_);

        for (int k = 0; k < j; ++k)
        {
            if (!flag)
            {
                int l = this.field_146216_j.length();
                i = this.field_146216_j.indexOf(32, i);

                if (i == -1)
                {
                    i = l;
                }
                else
                {
                    while (p_146197_3_ && i < l && this.field_146216_j.charAt(i) == 32)
                    {
                        ++i;
                    }
                }
            }
            else
            {
                while (p_146197_3_ && i > 0 && this.field_146216_j.charAt(i - 1) == 32)
                {
                    --i;
                }

                while (i > 0 && this.field_146216_j.charAt(i - 1) != 32)
                {
                    --i;
                }
            }
        }

        return i;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void func_146182_d(int p_146182_1_)
    {
        this.func_146190_e(this.field_146223_s + p_146182_1_);
    }

    /**
     * Sets the current position of the cursor.
     */
    public void func_146190_e(int p_146190_1_)
    {
        this.field_146224_r = p_146190_1_;
        int i = this.field_146216_j.length();
        this.field_146224_r = MathHelper.func_76125_a(this.field_146224_r, 0, i);
        this.func_146199_i(this.field_146224_r);
    }

    /**
     * Moves the cursor to the very start of this text box.
     */
    public void func_146196_d()
    {
        this.func_146190_e(0);
    }

    /**
     * Moves the cursor to the very end of this text box.
     */
    public void func_146202_e()
    {
        this.func_146190_e(this.field_146216_j.length());
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public boolean func_146201_a(char p_146201_1_, int p_146201_2_)
    {
        if (!this.field_146213_o)
        {
            return false;
        }
        else if (GuiScreen.func_175278_g(p_146201_2_))
        {
            this.func_146202_e();
            this.func_146199_i(0);
            
            KoreanCore.switchCombiningMode(false);// XXX
            
            return true;
        }
        else if (GuiScreen.func_175280_f(p_146201_2_))
        {
            GuiScreen.func_146275_d(this.func_146207_c());
            return true;
        }
        else if (GuiScreen.func_175279_e(p_146201_2_))
        {
            if (this.field_146226_p)
            {
                this.func_146191_b(GuiScreen.func_146277_j());
            }

            return true;
        }
        else if (GuiScreen.func_175277_d(p_146201_2_))
        {
            GuiScreen.func_146275_d(this.func_146207_c());

            if (this.field_146226_p)
            {
                this.func_146191_b("");
            }

            return true;
        }
        else
        {
            switch (p_146201_2_)
            {
                case 14:

                    if (GuiScreen.func_146271_m())
                    {
                        if (this.field_146226_p)
                        {
                            this.func_146177_a(-1);
                            
                            KoreanCore.switchCombiningMode(false);// XXX
                        }
                    }
                    else if (this.field_146226_p)
                    {
                    	Result r = KoreanCore.deleteKorean(func_146179_b(), func_146198_h(), func_146186_n());// XXX
                    	func_146180_a(r.STR);
                    	func_146190_e(r.CURSOR);
//                        this.deleteFromCursor(-1);
                    }

                    return true;
                case 199:

                    if (GuiScreen.func_146272_n())
                    {
                        this.func_146199_i(0);
                    }
                    else
                    {
                        this.func_146196_d();
                    }
                    
                    KoreanCore.switchCombiningMode(false);// XXX

                    return true;
                case 203:

                    if (GuiScreen.func_146272_n())
                    {
                        if (GuiScreen.func_146271_m())
                        {
                            this.func_146199_i(this.func_146183_a(-1, this.func_146186_n()));
                        }
                        else
                        {
                            this.func_146199_i(this.func_146186_n() - 1);
                        }
                    }
                    else if (GuiScreen.func_146271_m())
                    {
                        this.func_146190_e(this.func_146187_c(-1));
                    }
                    else
                    {
                        this.func_146182_d(-1);
                    }
                    
                    KoreanCore.switchCombiningMode(false);// XXX

                    return true;
                case 205:

                    if (GuiScreen.func_146272_n())
                    {
                        if (GuiScreen.func_146271_m())
                        {
                            this.func_146199_i(this.func_146183_a(1, this.func_146186_n()));
                        }
                        else
                        {
                            this.func_146199_i(this.func_146186_n() + 1);
                        }
                    }
                    else if (GuiScreen.func_146271_m())
                    {
                        this.func_146190_e(this.func_146187_c(1));
                    }
                    else
                    {
                        this.func_146182_d(1);
                    }
                    
                    KoreanCore.switchCombiningMode(false);// XXX

                    return true;
                case 207:

                    if (GuiScreen.func_146272_n())
                    {
                        this.func_146199_i(this.field_146216_j.length());
                    }
                    else
                    {
                        this.func_146202_e();
                    }
                    
                    KoreanCore.switchCombiningMode(false);// XXX

                    return true;
                case 211:

                    if (GuiScreen.func_146271_m())
                    {
                        if (this.field_146226_p)
                        {
                            this.func_146177_a(1);
                        }
                    }
                    else if (this.field_146226_p)
                    {
                        this.func_146175_b(1);
                    }
                    
                    KoreanCore.switchCombiningMode(false);// XXX

                    return true;
                case 29:// XXX
                	KoreanCore.switchInputMode();
                	
                	return true;
                default:

                    if (ChatAllowedCharacters.func_71566_a(p_146201_1_))
                    {
                        if (this.field_146226_p)
                        {
                        	Result r = KoreanCore.writeKorean(func_146179_b(), func_146198_h(), func_146186_n(), p_146201_1_);// XXX
                        	this.func_146180_a(r.STR);
                        	this.func_146190_e(r.CURSOR);
                        	this.func_146199_i(r.SELECTION);
//                            this.writeText(Character.toString(typedChar));
                        }

                        return true;
                    }
                    else
                    {
                        return false;
                    }
            }
        }
    }

    /**
     * Called when mouse is clicked, regardless as to whether it is over this button or not.
     */
    public void func_146192_a(int p_146192_1_, int p_146192_2_, int p_146192_3_)
    {
        boolean flag = p_146192_1_ >= this.field_146209_f && p_146192_1_ < this.field_146209_f + this.field_146218_h && p_146192_2_ >= this.field_146210_g && p_146192_2_ < this.field_146210_g + this.field_146219_i;

        if (this.field_146212_n)
        {
            this.func_146195_b(flag);
        }

        if (this.field_146213_o && flag && p_146192_3_ == 0)
        {
            int i = p_146192_1_ - this.field_146209_f;

            if (this.field_146215_m)
            {
                i -= 4;
            }

            String s = this.field_146211_a.func_78269_a(this.field_146216_j.substring(this.field_146225_q), this.func_146200_o());
            this.func_146190_e(this.field_146211_a.func_78269_a(s, i).length() + this.field_146225_q);
        }
    }

    /**
     * Draws the textbox
     */
    public void func_146194_f()
    {
        if (this.func_146176_q())
        {
            if (this.func_146181_i())
            {
                func_73734_a(this.field_146209_f - 1, this.field_146210_g - 1, this.field_146209_f + this.field_146218_h + 1, this.field_146210_g + this.field_146219_i + 1, -6250336);
                func_73734_a(this.field_146209_f, this.field_146210_g, this.field_146209_f + this.field_146218_h, this.field_146210_g + this.field_146219_i, -16777216);
            }

            int i = this.field_146226_p ? this.field_146222_t : this.field_146221_u;
            int j = this.field_146224_r - this.field_146225_q;
            int k = this.field_146223_s - this.field_146225_q;
            String s = this.field_146211_a.func_78269_a(this.field_146216_j.substring(this.field_146225_q), this.func_146200_o());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.field_146213_o && this.field_146214_l / 6 % 2 == 0 && flag;
            int l = this.field_146215_m ? this.field_146209_f + 4 : this.field_146209_f;
            int i1 = this.field_146215_m ? this.field_146210_g + (this.field_146219_i - 8) / 2 : this.field_146210_g;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (!s.isEmpty())
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = this.field_146211_a.func_175063_a(s1, (float)l, (float)i1, i);
            }

            boolean flag2 = this.field_146224_r < this.field_146216_j.length() || this.field_146216_j.length() >= this.func_146208_g();
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + this.field_146218_h : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length())
            {
                j1 = this.field_146211_a.func_175063_a(s.substring(j), (float)j1, (float)i1, i);
            }

            if (flag1)
            {
                if (flag2)
                {
                    Gui.func_73734_a(k1, i1 - 1, k1 + 1, i1 + 1 + this.field_146211_a.field_78288_b, -3092272);
                }
                else
                {
                    this.field_146211_a.func_175063_a("_", (float)k1, (float)i1, i);
                }
            }

            if (k != j)
            {
                int l1 = l + this.field_146211_a.func_78256_a(s.substring(0, k));
                this.func_146188_c(k1, i1 - 1, l1 - 1, i1 + 1 + this.field_146211_a.field_78288_b);
            }
        }
    }

    /**
     * Draws the current selection and a vertical line cursor in the text box.
     */
    private void func_146188_c(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_)
    {
        if (p_146188_1_ < p_146188_3_)
        {
            int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }

        if (p_146188_2_ < p_146188_4_)
        {
            int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }

        if (p_146188_3_ > this.field_146209_f + this.field_146218_h)
        {
            p_146188_3_ = this.field_146209_f + this.field_146218_h;
        }

        if (p_146188_1_ > this.field_146209_f + this.field_146218_h)
        {
            p_146188_1_ = this.field_146209_f + this.field_146218_h;
        }

        Tessellator tessellator = Tessellator.func_178181_a();
        VertexBuffer vertexbuffer = tessellator.func_178180_c();
        GlStateManager.func_179131_c(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.func_179090_x();
        GlStateManager.func_179115_u();
        GlStateManager.func_187422_a(GlStateManager.LogicOp.OR_REVERSE);
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        vertexbuffer.func_181662_b((double)p_146188_1_, (double)p_146188_4_, 0.0D).func_181675_d();
        vertexbuffer.func_181662_b((double)p_146188_3_, (double)p_146188_4_, 0.0D).func_181675_d();
        vertexbuffer.func_181662_b((double)p_146188_3_, (double)p_146188_2_, 0.0D).func_181675_d();
        vertexbuffer.func_181662_b((double)p_146188_1_, (double)p_146188_2_, 0.0D).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179134_v();
        GlStateManager.func_179098_w();
    }

    /**
     * Sets the maximum length for the text in this text box. If the current text is longer than this length, the
     * current text will be trimmed.
     */
    public void func_146203_f(int p_146203_1_)
    {
        this.field_146217_k = p_146203_1_;

        if (this.field_146216_j.length() > p_146203_1_)
        {
            this.field_146216_j = this.field_146216_j.substring(0, p_146203_1_);
        }
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int func_146208_g()
    {
        return this.field_146217_k;
    }

    /**
     * returns the current position of the cursor
     */
    public int func_146198_h()
    {
        return this.field_146224_r;
    }

    /**
     * Gets whether the background and outline of this text box should be drawn (true if so).
     */
    public boolean func_146181_i()
    {
        return this.field_146215_m;
    }

    /**
     * Sets whether or not the background and outline of this text box should be drawn.
     */
    public void func_146185_a(boolean p_146185_1_)
    {
        this.field_146215_m = p_146185_1_;
    }

    /**
     * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
     */
    public void func_146193_g(int p_146193_1_)
    {
        this.field_146222_t = p_146193_1_;
    }

    /**
     * Sets the color to use for text in this text box when this text box is disabled.
     */
    public void func_146204_h(int p_146204_1_)
    {
        this.field_146221_u = p_146204_1_;
    }

    /**
     * Sets focus to this gui element
     */
    public void func_146195_b(boolean p_146195_1_)
    {
        if (p_146195_1_ && !this.field_146213_o)
        {
            this.field_146214_l = 0;
        }

        this.field_146213_o = p_146195_1_;
    }

    /**
     * Getter for the focused field
     */
    public boolean func_146206_l()
    {
        return this.field_146213_o;
    }

    /**
     * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
     */
    public void func_146184_c(boolean p_146184_1_)
    {
        this.field_146226_p = p_146184_1_;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int func_146186_n()
    {
        return this.field_146223_s;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int func_146200_o()
    {
        return this.func_146181_i() ? this.field_146218_h - 8 : this.field_146218_h;
    }

    /**
     * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
     * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
     */
    public void func_146199_i(int p_146199_1_)
    {
        int i = this.field_146216_j.length();

        if (p_146199_1_ > i)
        {
            p_146199_1_ = i;
        }

        if (p_146199_1_ < 0)
        {
            p_146199_1_ = 0;
        }

        this.field_146223_s = p_146199_1_;

        if (this.field_146211_a != null)
        {
            if (this.field_146225_q > i)
            {
                this.field_146225_q = i;
            }

            int j = this.func_146200_o();
            String s = this.field_146211_a.func_78269_a(this.field_146216_j.substring(this.field_146225_q), j);
            int k = s.length() + this.field_146225_q;

            if (p_146199_1_ == this.field_146225_q)
            {
                this.field_146225_q -= this.field_146211_a.func_78262_a(this.field_146216_j, j, true).length();
            }

            if (p_146199_1_ > k)
            {
                this.field_146225_q += p_146199_1_ - k;
            }
            else if (p_146199_1_ <= this.field_146225_q)
            {
                this.field_146225_q -= this.field_146225_q - p_146199_1_;
            }

            this.field_146225_q = MathHelper.func_76125_a(this.field_146225_q, 0, i);
        }
    }

    /**
     * Sets whether this text box loses focus when something other than it is clicked.
     */
    public void func_146205_d(boolean p_146205_1_)
    {
        this.field_146212_n = p_146205_1_;
    }

    /**
     * returns true if this textbox is visible
     */
    public boolean func_146176_q()
    {
        return this.field_146220_v;
    }

    /**
     * Sets whether or not this textbox is visible
     */
    public void func_146189_e(boolean p_146189_1_)
    {
        this.field_146220_v = p_146189_1_;
    }
}
