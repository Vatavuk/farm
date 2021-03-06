/**
 * Copyright (c) 2016-2018 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.stk.pm.cost.funding

import com.jcabi.xml.XML
import com.zerocracy.Par
import com.zerocracy.Project
import com.zerocracy.cash.Cash
import com.zerocracy.farm.Assume
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.cost.Ledger

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Contributed by Stripe')
  ClaimIn claim = new ClaimIn(xml)
  Cash amount = new Cash.S(claim.param('amount'))
  new Ledger(project).bootstrap().add(
    new Ledger.Transaction(
      amount,
      'assets', 'cash',
      'income', "@${claim.author()}",
      'Contributed by Stripe'
    )
  )
  claim.copy()
    .type('Notify project')
    .param(
      'message',
      new Par(
        'The project %s has been funded via Stripe for %s;',
        'it was a free contribution of @%s, as in §50'
      ).say(project.pid(), amount, claim.author())
    )
    .postTo(project)
  claim.copy().type('Notify PMO').param(
    'message', new Par(
      'We just funded %s for %s via Stripe by @%s'
    ).say(project.pid(), amount, claim.author())
  ).postTo(project)
}
