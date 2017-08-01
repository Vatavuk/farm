/**
 * Copyright (c) 2016-2017 Zerocracy
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
package com.zerocracy.stk.pm.in.orders

import com.jcabi.xml.XML
import com.zerocracy.farm.Assume
import com.zerocracy.jstk.Project
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut
import com.zerocracy.pm.in.Orders

def exec(Project project, XML xml) {
  new Assume(project, xml).type('Start order')
  ClaimIn claim = new ClaimIn(xml)
  String job = claim.param('job')
  String login = claim.param('login')
  String reason = claim.param('reason')
  Orders orders = new Orders(project).bootstrap()
  if (orders.assigned(job)) {
    claim.reply(
      String.format(
        'Job `%s` is already assigned to @%s, sorry.',
        job, orders.performer(job)
      )
    ).postTo(project)
  } else {
    orders.assign(job, login, reason)
    claim.reply(
      String.format(
        'Job `%s` assigned to @%s, please go ahead' +
        ' ([policy](http://datum.zerocracy.com/pages/policy.html)).',
        job, login
      )
    ).postTo(project)
    new ClaimOut()
      .type('Order was given')
      .param('job', job)
      .param('login', login)
      .param('reason', reason)
      .postTo(project)
  }
}
