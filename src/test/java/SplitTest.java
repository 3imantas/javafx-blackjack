import com.example.blackjack.Models.GameLogic;
import com.example.blackjack.Controllers.UIHandler;
import com.example.blackjack.Models.Card;
import com.example.blackjack.Models.Hand;
import com.example.blackjack.Models.Player;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

public class SplitTest {
    Player player = new Player();
    @Mock
    UIHandler uiHandler;
    @InjectMocks
    GameLogic controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public SplitTest(){
        String suit = "hearts";
        String value = "3";
        String img = "assets/SVG-cards/" + value + "_of_" + suit + ".svg";
        Card card1 = new Card(suit, value, img, false);

        suit = "clubs";
        value = "3";
        img = "assets/SVG-cards/" + value + "_of_" + suit + ".svg";
        Card card2 = new Card(suit, value, img, false);

        player.getHand().setCards(new ArrayList<>(Arrays.asList(card1, card2)));
        controller = new GameLogic(player, uiHandler);

    }
    @Test
    public void cardsShouldBeEqual() {
        assertTrue(player.cardsAreEqual());
    }

    @Test
    public void playerHandShouldSplitIntoTwoHands(){
        doNothing().when(uiHandler).displayHandAndScore();
        controller.split(new HBox());
        assertEquals(2, player.getHands().size());
    }

    @Test
    public void eachPlayerHandShouldContainTwoCards(){
        doNothing().when(uiHandler).displayHandAndScore();
        controller.split(new HBox());
        controller.hit(player);
        controller.stand();
        controller.hit(player);

        List<Hand> totalHands = player.getHands();
        for(Hand hand :  totalHands){
            assertEquals(2, hand.getCards().size());
        }
    }

}
