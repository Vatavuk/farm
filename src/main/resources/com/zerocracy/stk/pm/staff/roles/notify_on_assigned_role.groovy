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
package com.zerocracy.stk.pm.staff.roles

import com.jcabi.xml.XML
import com.zerocracy.Farm
import com.zerocracy.Par
import com.zerocracy.Project
import com.zerocracy.farm.Assume
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.cost.Rates

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Role was assigned')
  ClaimIn claim = new ClaimIn(xml)
  String login = claim.param('login')
  String role = claim.param('role')
  Farm farm = binding.variables.farm
  String msg = new Par(
    farm,
    'You just got role %s in the %s project; '
  ).say(role, project.pid())
  Rates rates = new Rates(project).bootstrap()
  if (rates.exists(login)) {
    msg += new Par('your rate is %s').say(rates.rate(login))
  } else {
    msg += 'you work for free'
  }
  claim.copy()
    .type('Notify user')
    .token("user;${login}")
    .param('message', msg)
    .postTo(project)
}
