export function timeMinutesToText(minutes: number) {
    if (!minutes) {
        return "N/A";
    }
    let hours = 0;
    let days = 0; Math.floor(hours / 24);
    const parts = []
    if (minutes > 59) {
        hours = Math.floor(minutes / 60);
        minutes = minutes % 60;
    }
    if (hours > 23) {
        days = Math.floor(hours / 24);
        hours = hours % 24;
    }
    if (minutes) {
        parts.unshift(`${minutes} min.`);
    }
    if (hours) {
        parts.unshift(`${hours} hora${hours > 1 ? "s" : ""}`);
    }
    if (days) {
        parts.unshift(`${days} dia${days > 1 ? "s" : ""}`);
    }
    return parts.join(' ');
}