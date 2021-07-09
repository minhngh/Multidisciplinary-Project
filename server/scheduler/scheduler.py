import threading
import schedule
import time


class Scheduler:
    def __init__(self, interval_ms: int):
        self.interval_sec: float = interval_ms / 1000

        self._event_run = threading.Event()
        self._event_stop = threading.Event()

        threading.Thread(target=self._run).start()

    def _run(self):
        while not self._event_stop.is_set():
            if self._event_run.is_set():
                schedule.run_pending()
                time.sleep(self.interval_sec)
            else:
                self._event_run.wait(1)

    def pause(self):
        self._event_run.clear()

    def run(self):
        self._event_run.set()

    def stop(self):
        self._event_stop.set()
