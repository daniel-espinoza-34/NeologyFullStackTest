export function timeMinutesToText(minutes: number) {
    if (!minutes) {
        return "N/A";
    }
    if (minutes < 60) {
        return `${minutes} min.`;
    }
    let hours = Math.floor(minutes / 60);
    minutes = minutes % 60;
    if (hours < 24) {
        return `${hours} horas ${minutes} min.`;
    }
    let days = Math.floor(hours / 24);
    hours = hours % 24;
    return `${days} dias ${hours} horas ${minutes} min.`;
}