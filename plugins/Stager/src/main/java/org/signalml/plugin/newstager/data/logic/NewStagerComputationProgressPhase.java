package org.signalml.plugin.newstager.data.logic;

public enum NewStagerComputationProgressPhase {
	SIGNAL_STATS_PREPARE_PHASE,
	SIGNAL_STATS_SOURCE_FILE_INITIAL_READ_PHASE,
	SIGNAL_STATS_BLOCK_COMPUTATION_PHASE,

	BOOK_FILE_INITIAL_READ_PHASE,
	BOOK_PROCESSING_PHASE,

	TAG_WRITING_PREPARE_PHASE,
	TAG_WRITING_ALPHA,
	TAG_WRITING_DELTA,
	TAG_WRITING_SPINDLE,
	TAG_WRITING_SLEEP_PAGES,
	TAG_WRITING_CONSOLIDATED_SLEEP_PAGES,


	ABORT_PHASE,
}
