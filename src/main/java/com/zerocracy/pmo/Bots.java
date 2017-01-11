/**
 * Copyright (c) 2016 Zerocracy
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
package com.zerocracy.pmo;

import com.zerocracy.Xocument;
import com.zerocracy.jstk.Item;
import com.zerocracy.jstk.Project;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.json.JsonObject;
import org.xembly.Directives;

/**
 * Slack bots.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.7
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class Bots {

    /**
     * Project.
     */
    private final Project project;

    /**
     * Ctor.
     * @param pkt Project
     */
    public Bots(final Project pkt) {
        this.project = pkt;
    }

    /**
     * Bootstrap it.
     * @throws IOException If fails
     */
    public void bootstrap() throws IOException {
        try (final Item item = this.item()) {
            new Xocument(item.path()).bootstrap("bots", "pmo/bots");
        }
    }

    /**
     * Register new bot.
     * @param json JSON from Slack OAuth
     * @throws IOException If fails
     */
    public void register(final JsonObject json)
        throws IOException {
        final JsonObject bot = json.getJsonObject("bot");
        final String bid = bot.getString("bot_user_id");
        try (final Item item = this.item()) {
            new Xocument(item.path()).modify(
                new Directives()
                    .xpath(
                        String.format(
                            "/bots[not(bot[@id='%s'])]",
                            bid
                        )
                    )
                    .add("bot")
                    .attr("id", bid)
                    .xpath(
                        String.format(
                            "/bots/bot[@id='%s']",
                            bid
                        )
                    )
                    .addIf("access_token")
                    .set(json.getString("access_token")).up()
                    .addIf("team_name")
                    .set(json.getString("team_name")).up()
                    .addIf("team_id")
                    .set(json.getString("team_id")).up()
                    .addIf("bot_access_token")
                    .set(bot.getString("bot_access_token")).up()
                    .addIf("created")
                    .set(
                        ZonedDateTime.now().format(
                            DateTimeFormatter.ISO_INSTANT
                        )
                    )
            );
        }
    }

    /**
     * Get all bot access tokens.
     * @return Tokens
     * @throws IOException If fails
     */
    public Iterable<Map.Entry<String, String>> tokens() throws IOException {
        try (final Item item = this.item()) {
            return StreamSupport.stream(
                new Xocument(item.path()).nodes(
                    "/bots/bot"
                ).spliterator(),
                false
            ).map(
                node -> new HashMap.SimpleEntry<>(
                    node.xpath("@id").get(0),
                    node.xpath("bot_access_token/text()").get(0)
                )
            ).collect(Collectors.toList());
        }
    }

    /**
     * The item.
     * @return Item
     * @throws IOException If fails
     */
    private Item item() throws IOException {
        return this.project.acq("bots.xml");
    }

}
