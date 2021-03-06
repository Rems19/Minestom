package net.minestom.server.entity.ai.goal;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.utils.Position;

public class FollowTargetGoal extends GoalSelector {

    private Position lastPath;

    public FollowTargetGoal(EntityCreature entityCreature) {
        super(entityCreature);
    }

    @Override
    public boolean shouldStart() {
        return entityCreature.getTarget() != null;
    }

    @Override
    public void start() {
        final Entity target = entityCreature.getTarget();

        if (target != null) {
            final Position targetPosition = target.getPosition();
            if (targetPosition.getDistance(entityCreature.getPosition()) < 1) {
                entityCreature.setPathTo(null);
                return;
            }

            if (lastPath == null || lastPath.equals(targetPosition)) {
                entityCreature.setPathTo(targetPosition);
                lastPath = targetPosition;
            }
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean shouldEnd() {
        return true;
    }

    @Override
    public void end() {

    }
}
