package s05.virtualpet.model;

import jakarta.persistence.*;
import lombok.*;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetType;

import java.util.Random;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetType type; // HEARTS, DIAMONDS, CLUBS, SPADES

    @Enumerated(EnumType.STRING)
    private Luck luck = Luck.LUCKY; // Default: LUCKY

    private int chips = 100; // Default: 100 chips

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private static final int BET_COST = 15;
    private static final int WIN_BIG_AMOUNT = 50;
    private static final int ALL_IN_COST = 50;

    private static final Random random = new Random();

    public void placeBet() {
        this.chips = Math.max(this.chips - BET_COST, 0);
        updateLuck();
    }

    public void winBig() {
        this.chips = Math.min(this.chips + WIN_BIG_AMOUNT, 100);
        updateLuck();
    }

    public void goAllIn() {
        this.chips = Math.max(this.chips - ALL_IN_COST, 0);
        updateLuck();
    }

    private void updateLuck() {
        int luckFactor = random.nextInt(10); // Random luck modifier (0-9)

        if (this.chips == 0) {
            this.luck = Luck.BANKRUPT;
        } else if (this.chips <= 30) {
            this.luck = (luckFactor < 3) ? Luck.LUCKY : Luck.UNLUCKY; // 30% chance to be LUCKY
        } else if (this.chips >= 80) {
            this.luck = (luckFactor > 7) ? Luck.LUCKY : Luck.VERY_LUCKY; // 80% chance to be VERY_LUCKY
        } else {
            this.luck = (luckFactor > 6) ? Luck.VERY_LUCKY : Luck.LUCKY; // 70% chance to be LUCKY
        }
    }
}