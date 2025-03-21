package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.exception.custom.InvalidPetActionException;
import s05.virtualpet.model.Pet;
import s05.virtualpet.service.PetActionService;

import java.util.Random;

@Service
public class PetActionServiceImpl implements PetActionService {

    private static final int BET_COST = 15;
    private static final int WIN_BIG_AMOUNT = 50;
    private static final int ALL_IN_COST = 50;

    private final Random random = new Random();

    @Override
    public void applyAction(Pet pet, PetAction action) {
        switch (action) {
            case PLACE_BET -> placeBet(pet);
            case WIN_BIG -> {
                if (random.nextDouble() < 0.3) {
                    winBig(pet);
                } else {
                    placeBet(pet);
                }
            }
            case GO_ALL_IN -> goAllIn(pet);
            default -> throw new InvalidPetActionException("Unknown action: " + action);
        }

        updateLuck(pet);
    }

    private void placeBet(Pet pet) {
        pet.setChips(Math.max(pet.getChips() - BET_COST, 0));
    }

    private void winBig(Pet pet) {
        pet.setChips(Math.min(pet.getChips() + WIN_BIG_AMOUNT, 100));
    }

    private void goAllIn(Pet pet) {
        pet.setChips(Math.max(pet.getChips() - ALL_IN_COST, 0));
    }

    private void updateLuck(Pet pet) {
        int chips = pet.getChips();
        int luckFactor = random.nextInt(10);

        if (chips == 0) {
            pet.setLuck(Luck.BANKRUPT);
        } else if (chips <= 30) {
            pet.setLuck((luckFactor < 3) ? Luck.LUCKY : Luck.UNLUCKY);
        } else if (chips >= 80) {
            pet.setLuck((luckFactor > 7) ? Luck.LUCKY : Luck.VERY_LUCKY);
        } else {
            pet.setLuck((luckFactor > 6) ? Luck.VERY_LUCKY : Luck.LUCKY);
        }
    }
}