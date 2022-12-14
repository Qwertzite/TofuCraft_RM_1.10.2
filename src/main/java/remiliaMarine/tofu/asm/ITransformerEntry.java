package remiliaMarine.tofu.asm;

import java.util.EnumSet;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraftforge.fml.relauncher.Side;

public interface ITransformerEntry
{
    String getTargetClass();
    String getTargetMethodDeobf();
    String getTargetMethodObf();
    String getTargetMethodDesc();
    void transform(MethodNode mnode, ClassNode cnode);
    EnumSet<Side> getSide();
}
