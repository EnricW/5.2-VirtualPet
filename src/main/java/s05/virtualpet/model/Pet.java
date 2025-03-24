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
    private Luck luck = Luck.HAPPY; // Default: HAPPY

    private int chips = 100; // Default: 100 chips

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}