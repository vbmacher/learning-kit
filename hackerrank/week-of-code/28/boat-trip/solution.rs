use std::io::stdin;

fn read_numbers() -> Vec<i32> {
    let mut buffer = String::new();

    stdin().read_line(&mut buffer).expect("Expected N,C,M!");

    buffer
        .split_whitespace()
        .map(|c| c.parse::<i32>().unwrap())
        .collect()
}

fn main() {
    let numbers = read_numbers();
    let passengers = read_numbers();

    let (_,c,m) = (numbers[0], numbers[1], numbers[2]);

    let max_passengers = m * c;
    for group in passengers {
        if group > max_passengers {
            println!("No");
            return
        }
    }
    println!("Yes");
}