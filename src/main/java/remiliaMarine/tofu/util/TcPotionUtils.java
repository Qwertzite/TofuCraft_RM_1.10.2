package remiliaMarine.tofu.util;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class TcPotionUtils {
    /**
     * Returns {@code true} if {@code target} is present as an element anywhere in
     * {@code array}.
     *
     * @param array an array of {@code Potion} values, possibly empty
     * @param target a primitive {@code Potion} value
     * @return {@code true} if {@code array[i] == target} for some value of {@code
     *     i}
     */
    public static boolean contains(Potion[] array, Potion target) {
      for (Potion value : array) {
        if (value == target) {
          return true;
        }
      }
      return false;
    }
}
