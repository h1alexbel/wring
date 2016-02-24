/**
 * Copyright (c) 2016, wring.io
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the wring.io nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.wring.agents;

import com.jcabi.aspects.Tv;
import com.jcabi.log.Logger;
import io.wring.model.Event;
import io.wring.model.Events;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Events that ignores by regular expression.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id: c79829f9e91907f21c716854779af4233e496fa9 $
 * @since 0.4
 */
final class IgnoreEvents implements Events {

    /**
     * Origin.
     */
    private final transient Events origin;

    /**
     * Regex to ignore.
     */
    private final transient Pattern regex;

    /**
     * Ctor.
     * @param events Agent original
     * @param ptn Pattern
     */
    IgnoreEvents(final Events events, final String ptn) {
        this(events, Pattern.compile(ptn));
    }

    /**
     * Ctor.
     * @param events Agent original
     * @param ptn Pattern
     */
    IgnoreEvents(final Events events, final Pattern ptn) {
        this.origin = events;
        this.regex = ptn;
    }

    @Override
    public Iterable<Event> iterate() throws IOException {
        return this.origin.iterate();
    }

    @Override
    public void post(final String title, final String text) throws IOException {
        if (this.regex.matcher(text).find()) {
            Logger.info(
                this, "ignoring \"%s\" because of %s",
                StringUtils.abbreviate(text, Tv.FIFTY),
                this.regex
            );
        } else {
            this.origin.post(title, text);
        }
    }

    @Override
    public Event event(final String title) throws IOException {
        return this.origin.event(title);
    }
}
