package com.github.madaaraisok.guard.bot.domain.service.filter;

import com.github.madaaraisok.guard.bot.domain.model.filter.FilterEvent;
import com.github.madaaraisok.guard.bot.domain.model.filter.FilterScore;

interface FilterEvaluator {

    FilterScore evaluate(FilterEvent filterEvent);

}
