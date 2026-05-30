const els = {
  totalRows: document.querySelector("#totalRows"),
  totalRowsOut: document.querySelector("#totalRowsOut"),
  pageSize: document.querySelector("#pageSize"),
  pageSizeOut: document.querySelector("#pageSizeOut"),
  pageNumber: document.querySelector("#pageNumber"),
  pageNumberOut: document.querySelector("#pageNumberOut"),
  scanDifference: document.querySelector("#scanDifference"),
  cursorJumpNote: document.querySelector("#cursorJumpNote"),
  offsetScanned: document.querySelector("#offsetScanned"),
  cursorScanned: document.querySelector("#cursorScanned"),
  offsetExplanation: document.querySelector("#offsetExplanation"),
  cursorExplanation: document.querySelector("#cursorExplanation"),
  offsetRows: document.querySelector("#offsetRows"),
  cursorRows: document.querySelector("#cursorRows"),
  offsetValue: document.querySelector("#offsetValue"),
  cursorValue: document.querySelector("#cursorValue"),
  offsetReturned: document.querySelector("#offsetReturned"),
  cursorReturned: document.querySelector("#cursorReturned"),
  offsetSql: document.querySelector("#offsetSql"),
  cursorSql: document.querySelector("#cursorSql"),
  offsetApi: document.querySelector("#offsetApi"),
  cursorApi: document.querySelector("#cursorApi")
};

function formatNumber(value) {
  return new Intl.NumberFormat("en-US").format(value);
}

function rangeLabel(start, end) {
  if (start > end) return "none";
  if (start === end) return String(start);
  return `${start}-${end}`;
}

function idRangeLabel(values) {
  if (!values.length) return "none";
  return rangeLabel(values[0], values[values.length - 1]);
}

function buildRows(totalRows) {
  return Array.from({ length: totalRows }, (_, index) => index + 1);
}

function renderRows(container, rows, classify) {
  container.textContent = "";

  for (const id of rows) {
    const cell = document.createElement("span");
    const state = classify(id);
    cell.className = `row-cell ${state}`.trim();
    cell.textContent = id;
    cell.title = `id ${id}`;
    container.appendChild(cell);
  }
}

function buildOffsetSql(page, pageSize, offset) {
  return `-- Page ${page}: skip ${offset}, then return ${pageSize}
SELECT id
FROM posts
ORDER BY id ASC
LIMIT :limit OFFSET :offset;

-- Params
:limit  = ${pageSize}
:offset = ${offset}`;
}

function buildCursorSql(page, pageSize, afterId) {
  if (afterId === null) {
    return `-- Page ${page}: first page has no cursor
SELECT id
FROM posts
ORDER BY id ASC
LIMIT :limit;

-- Params
:limit = ${pageSize}`;
  }

  return `-- Page ${page}: continue after the previous page's last id
SELECT id
FROM posts
WHERE id > :after_id
ORDER BY id ASC
LIMIT :limit;

-- Params
:after_id = ${afterId}
:limit    = ${pageSize}`;
}

function update() {
  const totalRows = Number(els.totalRows.value);
  const pageSize = Number(els.pageSize.value);
  const rows = buildRows(totalRows);
  const maxPage = Math.max(1, Math.ceil(totalRows / pageSize));

  els.pageNumber.max = String(maxPage);
  const page = Math.min(Number(els.pageNumber.value), maxPage);
  els.pageNumber.value = String(page);

  const offset = (page - 1) * pageSize;
  const afterId = page === 1 ? null : offset;
  const returnedIds = rows.slice(offset, offset + pageSize);
  const offsetScannedIds = rows.slice(0, offset + returnedIds.length);
  const offsetSkippedIds = rows.slice(0, offset);
  const returnedSet = new Set(returnedIds);
  const offsetSkippedSet = new Set(offsetSkippedIds);

  els.totalRowsOut.textContent = `${formatNumber(totalRows)} rows`;
  els.pageSizeOut.textContent = `${formatNumber(pageSize)} per page`;
  els.pageNumberOut.textContent = `Page ${formatNumber(page)} of ${formatNumber(maxPage)}`;
  els.scanDifference.textContent = formatNumber(offsetSkippedIds.length);
  els.offsetScanned.textContent = formatNumber(offsetScannedIds.length);
  els.cursorScanned.textContent = formatNumber(returnedIds.length);
  els.offsetValue.textContent = formatNumber(offset);
  els.cursorValue.textContent = afterId === null ? "none" : `after_id=${formatNumber(afterId)}`;
  els.offsetReturned.textContent = idRangeLabel(returnedIds);
  els.cursorReturned.textContent = idRangeLabel(returnedIds);
  els.cursorJumpNote.textContent =
    afterId === null
      ? "On the first page there is no cursor yet, so both strategies start at ID 1."
      : `For this cursor request, after_id=${formatNumber(afterId)} tells MySQL where the previous page ended.`;

  els.offsetExplanation.textContent =
    `To return IDs ${idRangeLabel(returnedIds)}, MySQL scans IDs ${idRangeLabel(offsetScannedIds)}. ` +
    `It discards ${idRangeLabel(offsetSkippedIds)} and keeps ${idRangeLabel(returnedIds)}.`;

  els.cursorExplanation.textContent =
    afterId === null
      ? `To return IDs ${idRangeLabel(returnedIds)}, MySQL starts at the beginning and scans only those IDs.`
      : `The previous page ended at ID ${formatNumber(afterId)}. MySQL seeks to that position in the index, then scans IDs ${idRangeLabel(returnedIds)}.`;

  renderRows(els.offsetRows, rows, (id) => {
    if (offsetSkippedSet.has(id)) return "skipped";
    if (returnedSet.has(id)) return "returned-offset";
    return "";
  });

  renderRows(els.cursorRows, rows, (id) => {
    if (id === afterId) return "cursor-edge";
    if (returnedSet.has(id)) return "returned-cursor";
    return "";
  });

  els.offsetSql.textContent = buildOffsetSql(page, pageSize, offset);
  els.cursorSql.textContent = buildCursorSql(page, pageSize, afterId);
  els.offsetApi.textContent = `GET /api/posts?page=${page}&limit=${pageSize}

Server converts page to offset:
offset = (page - 1) * limit
offset = (${page} - 1) * ${pageSize} = ${offset}`;
  els.cursorApi.textContent =
    afterId === null
      ? `GET /api/posts?limit=${pageSize}

Server params:
limit required integer
after_id omitted on the first page`
      : `GET /api/posts?after_id=${afterId}&limit=${pageSize}

Server params:
after_id previous page's last id
limit    required integer`;
}

for (const control of [els.totalRows, els.pageSize, els.pageNumber]) {
  control.addEventListener("input", update);
}

update();
