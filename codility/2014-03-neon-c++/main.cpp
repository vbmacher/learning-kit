#include <vector>

using namespace std;


// you can also use includes, for example:
// #include <algorithm>

vector<int> getBoats(int len, int X) {
    vector<int> boats(len);
  
    int tmp = X;
    for (int j = 0; j < len; j++) {
        boats[j] = tmp;
        tmp += 2 * X;
    }
    return boats;
}

vector<int> getDist(vector<int> &R, vector<int> &boats) {
    int size = R.size();
    
    vector<int> dist(size);
    for (int i = 0; i < size; i++) {
        dist[i] = R[i] - boats[i];
    }
    return dist;
}

#define abs(x) (((x) < 0) ? -(x) : (x))

int getMaxDist(vector<int> &dist) {
    int max = 0;
    int size = dist.size();
    for (int i = 0; i < size; i++) {
        int d = dist[i];
        if (abs(d) > max) {
            max = abs(d);
        }
    }
    return max;
}

pair<int, int> getMaxDist(vector<int> &dist, int from, int to) {
    int max = 0;
    int origMax = 0;

    for (int j = from; j <= to; j++) {
        if (abs(dist[j]) > max) {
            max = abs(dist[j]);
            origMax = dist[j];
        }
    }
    return make_pair<int, int>(max, origMax);
}

vector<int> findMinims(vector<int> &B) {
    int size = B.size();
    vector<int> minims(size);

    int actualMinimum = 1000000000;
    for (int i = size - 1; i >= 0; i--) {
        if (B[i] < actualMinimum) {
            actualMinimum = B[i];
        }
        minims[i] = actualMinimum;
    }
    return minims;
}

int solution(vector<int> &R, int X, int M) {
    int size = R.size();
    if (M < (2 * X * size) || (size == 0)) {
        return -1;
    }

    vector<int> boats = getBoats(size, X);
    vector<int> dist = getDist(R, boats);

    int maxAllowedPosition = M - X;
    if (boats[size - 1] < maxAllowedPosition) {

        // those on the left - let's jump over, because they cannot improve their position
        int i = 0;
        while (i < size && dist[i] <= 0) {
            i++;
        }

        pair<int, int> maxDistLeft = getMaxDist(dist, 0, i - 1);
        pair<int, int> maxDistRight = getMaxDist(dist, i, size - 1);

        // if on the left the max distance is greater or equal to that on the right, it impossible to change it anyway.
        if (maxDistLeft.first >= maxDistRight.first) {
            return maxDistLeft.first;
        }
        // if the maximum is negative, it's impossible to change it - boats cannot be moved to the left
        if (maxDistRight.second < 0) {
            return maxDistRight.first;
        }

        vector<int> mins = findMinims(dist);

        int moving = 0;
        for (int j = 0; j < size; j++) {
            int can = (dist[j] - moving + (mins[j] - moving)) / 2;
            if (can > 0) {
                if (boats[size - 1] + moving + can <= maxAllowedPosition) {
                    moving += can;
                } else {
                    can = maxAllowedPosition - (boats[size - 1] + moving);
                    if (can > 0) {
                        moving += can;
                    }
                }
            }
            boats[j] += moving;
        }
        dist = getDist(R, boats);
    }

    // finally get max dist
    return getMaxDist(dist);
}




int main(int argc, char** argv) {

    return 0;
}

