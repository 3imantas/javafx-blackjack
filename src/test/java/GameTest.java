import com.example.blackjack.Controllers.MainController;
import com.example.blackjack.Models.Dealer;
import com.example.blackjack.Models.Deck;
import com.example.blackjack.Models.Participant;
import com.example.blackjack.Models.Player;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameTest {
    Player player = new Player(5000);
    Dealer dealer = Dealer.getInstance();
    Deck deck = new Deck();
    @Test
    public void playerShouldWinTwiceAmountOfBet() {
        MainController controller = new MainController(deck, player, dealer);
        player.setScore(21);
        dealer.setScore(20);
        player.setBet(9999);
        controller.checkResult(player);
        assertEquals(19998, player.getWon());
    }

    @Test
    public void playerShouldWinSameAmountOfBet() {
        MainController controller = new MainController(deck, player, dealer);
        player.setScore(20);
        dealer.setScore(20);
        player.setBet(444);
        controller.checkResult(player);
        assertEquals(444, player.getWon());
    }

    @Test
    public void playerShouldWinZero() {
        MainController controller = new MainController(deck, player, dealer);
        player.setScore(2);
        dealer.setScore(4);
        player.setBet(300);
        controller.checkResult(player);
        assertEquals(0, player.getWon());
    }


}
