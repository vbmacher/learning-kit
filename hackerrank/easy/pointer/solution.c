#include <stdio.h>

#define abs(a) ((a) < 0) ? (-(a)) : (a)

void update(int *a,int *b) {
    int tmpa =*a;
    (*a) = tmpa + (*b);
    (*b) = abs(tmpa - (*b));
}

int main() {
    int a, b;
    int *pa = &a, *pb = &b;
    
    scanf("%d %d", &a, &b);
    update(pa, pb);
    printf("%d\n%d", a, b);

    return 0;
}

