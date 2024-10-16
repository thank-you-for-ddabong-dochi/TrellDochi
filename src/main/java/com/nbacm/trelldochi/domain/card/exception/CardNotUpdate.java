package com.nbacm.trelldochi.domain.card.exception;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public class CardNotUpdate extends ForbiddenException {
    public CardNotUpdate(ObjectOptimisticLockingFailureException e) {
        super("다른사람이 수정중입니다." + e);
    }
}
