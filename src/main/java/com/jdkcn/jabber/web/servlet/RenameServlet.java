/**
 * Copyright (c) 2005-2013, Rory Ye
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the Jdkcn.com nor the names of its contributors may
 *       be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jdkcn.jabber.web.servlet;

import com.google.inject.Singleton;
import com.jdkcn.jabber.robot.Robot;
import com.jdkcn.jabber.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * rename the roster entry in the robot.
 * User: rory
 * Date: 4/15/13
 * Time: 09:08
 */
@Singleton
public class RenameServlet extends HttpServlet {

    private static final long serialVersionUID = 8242051448548625372L;

	@Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String robotName = req.getParameter("robotName");
        String entry = req.getParameter("entry");
        String newName = req.getParameter("newname");
        if (StringUtils.isNotBlank(robotName) && StringUtils.isNotBlank(entry) && StringUtils.isNotBlank(newName)) {

            @SuppressWarnings("unchecked")
			List<Robot> allRobots = (List<Robot>) req.getServletContext().getAttribute(Constants.ROBOTS);
            Robot robot = null;
            for (Robot r : allRobots) {
                if (StringUtils.equals(r.getName(), robotName)) {
                    robot = r;
                }
            }

            if (robot != null) {
                Roster roster = robot.getConnection().getRoster();
                RosterEntry rosterEntry = roster.getEntry(entry);
                if (rosterEntry != null) {
                    rosterEntry.setName(newName);
                }
                robot.getRosters().clear();
                robot.getRosters().addAll(roster.getEntries());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
