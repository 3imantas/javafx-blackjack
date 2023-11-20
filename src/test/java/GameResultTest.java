import com.example.blackjack.Models.GameLogic;
import com.example.blackjack.Controllers.UIHandler;
import com.example.blackjack.Models.Dealer;
import com.example.blackjack.Models.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameResultTest {
    Player player = new Player();
    Dealer dealer = Dealer.getInstance();

    UIHandler uiHandler;

    GameLogic controller = new GameLogic(player, uiHandler);

    @Test
    public void playerShouldWinTwiceAmountOfBet() {
        player.getHand().setScore(21);
        dealer.getHand().setScore(20);
        player.setBet(9999);
        controller.checkResult();
        assertEquals(19998, player.getWon());
    }

    @Test
    public void playerShouldWinSameAmountOfBet() {
        player.getHand().setScore(20);
        dealer.getHand().setScore(20);
        player.setBet(444);
        controller.checkResult();
        assertEquals(444, player.getWon());
    }

    @Test
    public void playerShouldWinZero() {
        player.getHand().setScore(2);
        dealer.getHand().setScore(4);
        player.setBet(300);
        controller.checkResult();
        assertEquals(0, player.getWon());
    }


}
