package com.ctb.contentretriever.util;

import java.io.Closeable;
import java.io.IOException;

public class ClosableHelper {

	static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}
}
