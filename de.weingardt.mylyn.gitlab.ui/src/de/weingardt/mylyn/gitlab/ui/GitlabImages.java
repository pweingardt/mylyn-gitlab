/*******************************************************************************
 * Copyright (c) 2014, Paul Weingardt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Paul Weingardt - initial API and implementation
 *******************************************************************************/



package de.weingardt.mylyn.gitlab.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Shawn Minto
 * @author Mik Kersten
 * @author Steffen Pingel
 */
public class GitlabImages {

	private static final URL baseURL = GitlabUIPlugin.getDefault().getBundle().getEntry("/icons/"); //$NON-NLS-1$

	public static final ImageDescriptor OVERLAY_BUG = create("overlay-bug.gif"); //$NON-NLS-1$

	public static final ImageDescriptor OVERLAY_FEATURE = create("overlay-feature.gif"); //$NON-NLS-1$

	public static final ImageDescriptor OVERLAY_STORY = create("overlay-story.gif"); //$NON-NLS-1$
	
	public static final Image IMAGE_LABEL = create("label.png").createImage();

	private static ImageDescriptor create(String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	private static URL makeIconFileURL(String name) throws MalformedURLException {
		if (baseURL == null) {
			throw new MalformedURLException();
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append(name);
		return new URL(baseURL, buffer.toString());
	}

}
