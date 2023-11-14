import com.example.blackjack.Controllers.MainController;
import com.example.blackjack.Models.Dealer;
import com.example.blackjack.Models.Deck;
import com.example.blackjack.Models.Participant;
import com.example.blackjack.Models.Player;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {
    Player player = new Player(5000);
    Dealer dealer = Dealer.getInstance();
    Deck deck = new Deck();
    @Test
    public void playerShouldNotBeNullWhenCreated() {
        assertNotNull(player);
    }

    @Test
    public void playerShouldReceiveOneCardWhenHit(){
        MainController controller = new MainController(deck, player, dealer);
        controller.hit(player);
        assertEquals(1, player.getHand().size());
    }


}
