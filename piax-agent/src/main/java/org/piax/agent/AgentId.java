/*
 * AgentId.java - An identifier of an agent.
 * 
 * Copyright (c) 2009-2015 PIAX development team
 * Copyright (c) 2006-2008 Osaka University
 * Copyright (c) 2004-2005 BBR Inc, Osaka University
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to 
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY 
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * $Id: AgentId.java 1168 2015-05-17 12:20:03Z teranisi $
 */

package org.piax.agent;

import org.piax.common.ObjectId;

/**
 * An identifier of an agent.
 */
public class AgentId extends ObjectId {
    private static final long serialVersionUID = -1L;
    public static int DEFAULT_BYTE_LENGTH = 16;

    public static AgentId newId() {
        return new AgentId(newRandomBytes(DEFAULT_BYTE_LENGTH));
    }

    public AgentId(byte[] bytes) {
        super(bytes);
    }
    
    public AgentId(String id) {
        super(id);
    }
}
