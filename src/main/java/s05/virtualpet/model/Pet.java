package s05.virtualpet.model;

import jakarta.persistence.*;
import lombok.*;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetType;

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

    public void placeBet() {
        this.chips = Math.max(this.chips - 15, 0);
        updateLuck();
    }

    public void winBig() {
        this.chips = Math.min(this.chips + 50, 100);
        updateLuck();
    }

    public void goAllIn() {
        this.chips = Math.max(this.chips - 50, 0);
        updateLuck();
    }

    private void updateLuck() {
        if (this.chips == 0) {
            this.luck = Luck.BANKRUPT;
        } else if (this.chips <= 30) {
            this.luck = Luck.UNLUCKY;
        } else if (this.chips >= 80) {
            this.luck = Luck.VERY_LUCKY;
        } else {
            this.luck = Luck.LUCKY;
        }
    }
}