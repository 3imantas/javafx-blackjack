import com.example.blackjack.Models.GameLogic;
import com.example.blackjack.Controllers.UIHandler;
import com.example.blackjack.Models.Dealer;
import com.example.blackjack.Models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class ParticipantTest {
    Player player = new Player();

    Dealer dealer = Dealer.getInstance();
    @Mock
    UIHandler uiHandler;
    @InjectMocks
    GameLogic controller = new GameLogic(player, uiHandler);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void playerShouldNotBeNullWhenCreated() {
        assertNotNull(player);
    }

    @Test
    public void dealerShouldNotBeNullWhenCreated() {
        assertNotNull(dealer);
    }

    @Test
    public void playerShouldReceiveOneCardWhenHit(){
        doNothing().when(uiHandler).displayHandAndScore();
        controller.hit(player);
        assertEquals(1, player.getHand().getCards().size());
    }

    @Test
    public void dealerShouldReceiveOneCardWhenHit(){
        doNothing().when(uiHandler).displayHandAndScore();
        controller.hit(dealer);
        assertEquals(1, dealer.getHand().getCards().size());
    }


}
